package imageanalysis;

import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.nio.IntBuffer;

import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacv.cpp.cvkernels;
import com.googlecode.javacv.cpp.cvkernels.KernelData;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.IplImageArray;

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
		//System.out.println(".");
		
		IplImage imgA = IplImage.createFrom(img);
	/*	
		IplImage imgD = cvCreateImage(imgA.cvSize(), imgA.depth(), imgA.nChannels()	);
		
		cvSet(imgD, CV_RGB (1.0,1.0,1.0));
		IplImage imgS = cvCreateImage(imgA.cvSize(), imgA.depth(), imgA.nChannels()	);
		
		cvMul(imgA, imgD , imgS, 1) ;
	*/
		
		CvSize size = imgA.cvSize() ;
		/*for (int i = 0 ; i < size.height() * size.width() ; ++i )
			System.out.println("Avant " + cvGet1D(imgA, i)) ;*/
		
		/*for (int i = 0 ; i < size.width() ; ++i )
			for (int j = 0 ; j <  size.height() ; ++j )
				cvSet2D(imgA, j, i , CV_RGB (0,255,0));
		*/
		
		
		/*
		for (int i = 0 ; i < 4 * size.height()*size.width() / 5 ; i++) {
			int posI = (int) Math.random() % size.width() ;
			int posJ = (int) Math.random() % size.height() ;
			//cvSet2D(imgA, posJ , posI , CV_RGB (Math.random(),Math.random(),Math.random()));
			cvSet2D(imgA, posJ , posI , CV_RGB (0,0,0));
		} */
	
		/*double div = 128 ;
		
		for (int i = 0 ; i < size.width() ; ++i )
			for (int j = 0 ; j <  size.height() ; ++j ) {
				CvScalar val = cvGet2D(imgA, j, i) ;
				for (int z = 0 ; z < val.capacity() ; ++ z)
					val.setVal(z,val.getVal(z) - val.getVal(z) % div + div/2);
				cvSet2D(imgA, j, i , val );
			}*/

		FloatPointer myPtr = new FloatPointer(3*3);
		CvMat m = cvMat(3, 3, CV_32F , myPtr) ;
		m.put(1, 1, 5.0	);
		m.put(0, 1, -1	);
		m.put(2, 1, -1	);
		m.put(1, 0, -1	);
		m.put(1, 2, -1 );
		
		cvFilter2D(imgA, imgA ,m , cvPoint(-1, -1));
		
		
		/*for (int i = 0 ; i < size.height() * size.width() ; ++i )
			System.out.println("Apres " + cvGet1D(imgA, i)) ;*/
		
		cvNamedWindow( "My image", 0 );
        cvShowImage( "LKpyr_OpticalFlow", imgA );
        cvWaitKey(100) ;
	}

	@Override
	public void setIImageResoning(IImageReasoning imgR) {
		// TODO Auto-generated method stub
		this.imgReasoning = imgR ;
	}

}
