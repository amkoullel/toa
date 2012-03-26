package imageacquistionvideo;

import java.awt.image.BufferedImage;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

import interfaces.IImageAcquisition;
import interfaces.IImageAnalysis;

public class ImageAcquisitionVideo implements IImageAcquisition {
	private IImageAnalysis imgAnalyse ;
	private String file_name ;

	public ImageAcquisitionVideo() {
		// TODO Auto-generated constructor stub
		//file_name = "/home/algassimou/test1.flv" ;
		file_name = "/comptes/E07A946C/test.avi" ;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// TODO Auto-generated method stub

		IContainer container = IContainer.make() ;
		if (container.open(this.file_name , IContainer.Type.READ , null) < 0)
			throw new IllegalArgumentException("Impossible d'ouvrir le fichier"+this.file_name);

		
		int numStreams = container.getNumStreams() ;
		int videoStreamId = -1 ;
		IStreamCoder videoCoder = null ;
		
		// Recherche du flux video dans le fichier 
		for (int i = 0 ; i < numStreams ; ++i){
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder() ;
			
			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoCoder = coder ;
				videoStreamId = i ;
				break ;
			}
		}
		
		if (videoStreamId == -1 )
			throw new RuntimeException ("Impossible de trouver le flux video dans le conteneur" +container);
		
		if (videoCoder.open() < 0)
			throw new RuntimeException ("Impossible d'ouvrir le flux video du conteneur" + container);
		
		IVideoResampler resampler = null ;
		
		//Convertir la video si le flux n'est pas encoder en RGB24
		
		if (videoCoder.getPixelType() != IPixelFormat.Type.RGB24) {
			resampler = IVideoResampler.make(
					videoCoder.getWidth(), videoCoder.getHeight() , IPixelFormat.Type.RGB24,
					videoCoder.getWidth(), videoCoder.getHeight() , videoCoder.getPixelType()) ;
			
			if (resampler == null)
				throw new RuntimeException ("Impossible d'obtenir un resampler ");
		}
		
		
		//Lit la video image par image
		
		IPacket packet = IPacket.make() ;
		while (container.readNextPacket(packet) >= 0)
		{
			if (packet.getStreamIndex() == videoStreamId) {
				IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType() , 
						videoCoder.getWidth() , videoCoder.getHeight()) ;
			
				int offset = 0 ;
				
				while (offset < packet.getSize()) {
					int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset) ;
					if (bytesDecoded < 0)
						throw new RuntimeException ("Impossible de decoder la video");
					
					offset += bytesDecoded ;
					if (picture.isComplete())
					{
						IVideoPicture newPicture = null ;
						if (picture.getPixelType() == IPixelFormat.Type.RGB24)
						{
							newPicture = IVideoPicture.make( IPixelFormat.Type.RGB24, 
									videoCoder.getWidth() , videoCoder.getHeight());	
						
							if (resampler.resample(newPicture, picture) < 0)
								throw new IllegalArgumentException("Impossible de convertir l'image") ;
						}
						else 
							newPicture = picture ;
						
						 // Convert the BGR24 to an Java buffered image	            
			            IConverter converter = ConverterFactory.createConverter(
			            	      ConverterFactory.XUGGLER_BGR_24, newPicture);
			            
			            BufferedImage javaImage = converter.toImage(newPicture);

						this.imgAnalyse.analyse(javaImage);
					}
				}
			}
		}
		
		// terminer on envoie null
		this.imgAnalyse.analyse(null);
	}

	@Override
	public void setIImageAnalysis(IImageAnalysis analyse) {
		// TODO Auto-generated method stub
		this.imgAnalyse = analyse ;
		//this.imgAnalyse = new TestAnalyse() ;
	}

}
