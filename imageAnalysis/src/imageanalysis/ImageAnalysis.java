package imageanalysis;

import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import interfaces.IImageAnalysis;
import interfaces.IImageReasoning;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_video.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class ImageAnalysis implements IImageAnalysis {
	private IImageReasoning imgReasoning ;
	
	public ImageAnalysis() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void analyse(BufferedImage img) {
		// TODO Auto-generated method stub
		System.out.println(".");
		
		IplImage imgA = IplImage.createFrom(img);
		cvNamedWindow( "My image", 0 );
        cvShowImage( "LKpyr_OpticalFlow", imgA );
        cvWaitKey(0) ;
	}

	@Override
	public void setIImageResoning(IImageReasoning imgR) {
		// TODO Auto-generated method stub
		this.imgReasoning = imgR ;
	}

}
