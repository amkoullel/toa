package imageanalysis;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_legacy;


import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.cpp.cvkernels;
import com.googlecode.javacv.cpp.cvkernels.KernelData;
import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_ml.CvSVMKernel.Calc;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHidHaarClassifierCascade;

import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;

import com.googlecode.javacv.* ;


import interfaces.IImageAnalysis;
import interfaces.IImageReasoning;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
//import static com.googlecode.javacv.cpp.opencv_video.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class ImageAnalysis implements IImageAnalysis {
	private IImageReasoning imgReasoning ;
	private CvHistogram hist ;
	private int image_count ;
	private CvMemStorage storage ;
	private CvHaarClassifierCascade cascade ;
	
	public ImageAnalysis() {
		// TODO Auto-generated constructor stub
		imgReasoning = null ;
		image_count = 0 ;
		
		cvNamedWindow( "My image", 0 );
		
		int nBins = 256 ;
        int dims = 3 ;
        int[] size = {nBins,nBins,nBins};
       // float ch_range[] = {0, 255};
        float[][] ranges = {{0, 255} , {0, 255} , {0, 255}};

       hist  = cvCreateHist(dims,size,CV_HIST_SPARSE, ranges, 1);
       String file_templates = "/comptes/E07A946C/logiciel/OpenCV-2.3.1/data/haarcascades/haarcascade_frontalface_alt.xml" ;
    // We instantiate a classifier cascade to be used for detection, using the cascade definition.
       cascade = new CvHaarClassifierCascade(cvLoad(file_templates));
       storage = CvMemStorage.create();
       
      // System.out.println("Creation");
	}

	@Override
	public void analyse(BufferedImage img) {
		// TODO Auto-generated method stub
		//System.out.println(".");
		
		IplImage imgA = IplImage.createFrom(img);	
		System.out.println("raisone ") ; 
		IplImage grayImage = IplImage.create(imgA.cvSize().width() , imgA.cvSize().height() , IPL_DEPTH_8U, 1);
		
		// We convert the original image to grayscale.
		cvCvtColor(imgA, grayImage, CV_BGR2GRAY);
		
		System.out.println("image " + image_count++) ;	
		
		// We detect the faces.
		CvSeq faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1, 0);
		
		ArrayList<Object> a = new ArrayList<Object> () ;
		for (int i = 0; i < faces.total(); i++) {
			a.add(cvGetSeqElem(faces, i)) ;
		}
		
		
		
		this.imgReasoning.reasonnig(a) ;
	 
        try {
        	cvShowImage( "LKpyr_OpticalFlow", imgA );
        	cvWaitKey(3) ;
        	cvReleaseImage(grayImage);
        } catch (Exception e) {
        	
        }             
	}

	@Override
	public void setIImageResoning(IImageReasoning imgR) {
		// TODO Auto-generated method stub
		this.imgReasoning = imgR ;
	}

}
