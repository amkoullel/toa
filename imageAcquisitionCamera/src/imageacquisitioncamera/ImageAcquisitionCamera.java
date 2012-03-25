package imageacquisitioncamera;

import java.awt.image.BufferedImage;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IError;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;


import interfaces.IImageAcquisition;
import interfaces.IImageAnalysis;

public class ImageAcquisitionCamera implements IImageAcquisition {
	private String driverName ;
	private String deviceName ;
	private IImageAnalysis imgAnalyse;

	public ImageAcquisitionCamera() {
		// TODO Auto-generated constructor stub
		
		String name = System.getProperty ( "os.name" );

		if (name.equals("Linux")) {
			this.driverName = "video4linux2" ;
			this.deviceName = "/dev/video0" ;
		}
		else if (name.equals("Window")) {
			this.driverName = "vfwcap" ;
			this.deviceName = "0" ; 
		}
	}
	
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		IContainer container = IContainer.make();
		
	    // Devices, unlike most files, need to have parameters set in order
	    // for Xuggler to know how to configure them.  For a webcam, these
	    // parameters make sense
		
	    
	    // The timebase here is used as the camera frame rate
	   // params.setTimeBase(IRational.make(30,1));
		container.setProperty("TimeBase", IRational.make(50,1)) ;
		
	    // we need to tell the driver what video with and height to use
	   // params.setVideoWidth(320);
		
		container.setProperty("SetVideoWidth", 320) ;
	   // params.setVideoHeight(240);
		container.setProperty("setVideoHeight", 240) ;
		
	    // and finally, we set these parameters on the container before opening
	   // container.setParameters(params);
	    
	    // Tell Xuggler about the device format
	    IContainerFormat format = IContainerFormat.make();
	    if (format.setInputFormat(driverName) < 0)
	      throw new IllegalArgumentException("couldn't open webcam device: " + driverName);
	    
	    // Open up the container
	    int retval = container.open(deviceName, IContainer.Type.READ, format);
	    if (retval < 0)
	    {
	      // This little trick converts the non friendly integer return value into
	      // a slightly more friendly object to get a human-readable error name
	      IError error = IError.make(retval);
	      throw new IllegalArgumentException("could not open file: " + deviceName + "; Error: " + error.getDescription());
	    }      

	    // query how many streams the call to open found
	    int numStreams = container.getNumStreams();

	    // and iterate through the streams to find the first video stream
	    int videoStreamId = -1;
	    IStreamCoder videoCoder = null;
	    for(int i = 0; i < numStreams; i++)
	    {
	      // Find the stream object
	      IStream stream = container.getStream(i);
	      // Get the pre-configured decoder that can decode this stream;
	      IStreamCoder coder = stream.getStreamCoder();

	      if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
	      {
	        videoStreamId = i;
	        videoCoder = coder;
	        break;
	      }
	    }
	    if (videoStreamId == -1)
	      throw new RuntimeException("could not find video stream in container: "+deviceName);

	    /*
	     * Now we have found the video stream in this file.  Let's open up our decoder so it can
	     * do work.
	     */
	    if (videoCoder.open() < 0)
	      throw new RuntimeException("could not open video decoder for container: "+deviceName);

	    IVideoResampler resampler = null;
	    if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24)
	    {
	      // if this stream is not in BGR24, we're going to need to
	      // convert it.  The VideoResampler does that for us.
	      resampler = IVideoResampler.make(videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24,
	          videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
	      if (resampler == null)
	        throw new RuntimeException("could not create color space resampler for: " + deviceName);
	    }

	    /*
	     * Now, we start walking through the container looking at each packet.
	     */
	    IPacket packet = IPacket.make();
	    while(container.readNextPacket(packet) >= 0)
	    {
	      /*
	       * Now we have a packet, let's see if it belongs to our video stream
	       */
	      if (packet.getStreamIndex() == videoStreamId)
	      {
	        /*
	         * We allocate a new picture to get the data out of Xuggler
	         */
	        IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
	            videoCoder.getWidth(), videoCoder.getHeight());

	        int offset = 0;
	        while(offset < packet.getSize())
	        {
	          /*
	           * Now, we decode the video, checking for any errors.
	           * 
	           */
	          int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
	          if (bytesDecoded < 0)
	            throw new RuntimeException("got error decoding video in: " + deviceName);
	          offset += bytesDecoded;

	          /*
	           * Some decoders will consume data in a packet, but will not be able to construct
	           * a full video picture yet.  Therefore you should always check if you
	           * got a complete picture from the decoder
	           */
	          if (picture.isComplete())
	          {
	            IVideoPicture newPic = picture;
	            /*
	             * If the resampler is not null, that means we didn't get the video in BGR24 format and
	             * need to convert it into BGR24 format.
	             */
	            if (resampler != null)
	            {
	              // we must resample
	              newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), picture.getWidth(), picture.getHeight());
	              if (resampler.resample(newPic, picture) < 0)
	                throw new RuntimeException("could not resample video from: " + deviceName);
	            }
	            if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
	              throw new RuntimeException("could not decode video as BGR 24 bit data in: " + deviceName);

	            // Convert the BGR24 to an Java buffered image	            
	            IConverter converter = ConverterFactory.createConverter(
	            	      ConverterFactory.XUGGLER_BGR_24, newPic);
	            
	            BufferedImage javaImage = converter.toImage(newPic);
	            this.imgAnalyse.analyse(javaImage);
	          }
	        }
	      }
	      else
	      {
	        /*
	         * This packet isn't part of our video stream, so we just silently drop it.
	         */
	        do {} while(false);
	      }
		
	    }
		
	}

	@Override
	public void setIImageAnalysis(IImageAnalysis analyse) {
		// TODO Auto-generated method stub
		this.imgAnalyse = analyse ;
	}

}
