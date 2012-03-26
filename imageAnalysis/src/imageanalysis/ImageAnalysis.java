package imageanalysis;

import java.awt.image.BufferedImage;

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
       
       System.out.println("Creation");
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
		
		/*CvSize size = imgA.cvSize() ;
		for (int i = 0 ; i < size.height() * size.width() ; ++i )
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

		/*FloatPointer myPtr = new FloatPointer(3*3);
		CvMat m = cvMat(3, 3, CV_32F , myPtr) ;
		m.put(1, 1, 5.0	);
		m.put(0, 1, -1	);
		m.put(2, 1, -1	);
		m.put(1, 0, -1	);
		m.put(1, 2, -1 );
		
		cvFilter2D(imgA, imgA ,m , cvPoint(0, 0));*/
	
		/*int dims = 3 ;
        
        IplImage [] chanels = new IplImage [dims] ;
        for (int cpt = 0 ; cpt < dims ; cpt++) {
        	chanels[cpt] = cvCreateImage(imgA.cvSize(), imgA.depth(), 1);
        }
        
        
		cvSplit(imgA, chanels[0], chanels[1], chanels[2], null);
                
        cvCalcHist(chanels , hist, 0,null);
        
        cvThreshold(imgA, imgA ,  100, 255, CV_THRESH_BINARY_INV);
        
		*/
		
		

		IplImage grayImage = IplImage.create(imgA.cvSize().width() , imgA.cvSize().height() , IPL_DEPTH_8U, 1);
		
		// We convert the original image to grayscale.
		cvCvtColor(imgA, grayImage, CV_BGR2GRAY);
		
		System.out.println("image " + image_count++) ;	
		
		// We detect the faces.
		 CvSeq faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1, 0);
			 
		//We iterate over the discovered faces and draw yellow rectangles around them.
		for (int i = 0; i < faces.total(); i++) {
		  CvRect r = new CvRect(cvGetSeqElem(faces, i));
		   cvRectangle(imgA, cvPoint(r.x(), r.y()),
		cvPoint(r.x() + r.width(), r.y() + r.height()), CvScalar.YELLOW, 1, CV_AA, 0);
		}
			 
	 // Save the image to a new file.
		//cvSaveImage(imgA , dest);
		
		
		
		/*CvRect rect = new CvRect(0, 0, 100, 150) ;
		
		System.out.println("Size " + imgA.cvSize().width() + " " + imgA.cvSize().height());
		
		cvSetImageROI(imgA, rect) ;

		System.out.println("coucou 1");
			
		int dims = 3 ;
		
        IplImage [] chanels = new IplImage [dims] ;
        IplImage [] channels = new IplImage [dims] ;
        
        for (int cpt = 0 ; cpt < dims ; cpt++) {
        	chanels[cpt] = cvCreateImage(cvGetSize(imgA), imgA.depth(), 1);
        }
         
		cvSplit(imgA, chanels[0], chanels[1], chanels[2], null); 
		
		System.out.println("coucou 2");
		
		int cpt = 0 ;
		for (IplImage src : chanels) {
			 channels[cpt] = cvCreateImage(cvGetSize(src), IPL_DEPTH_32F, src.nChannels());
		     cvConvertScale(src,channels[cpt] , 1, 0) ;
		     cpt++ ;
		}
		
        cvCalcHist(channels , hist, 0,null);
        cvNormalizeHist(hist, 1.0);
        
        
         cvResetImageROI(imgA);
       
        // convertion
        IplImage conv = cvCreateImage(cvGetSize(imgA), IPL_DEPTH_32F , imgA.nChannels());
        cvConvertScale(imgA, conv, 1, 0);
        
       // IplImage [] channels = new IplImage [dims] ;
        for (cpt = 0 ; cpt < dims ; cpt++) {
        	channels[cpt] = cvCreateImage(cvGetSize(conv), conv.depth(), 1);
        }
       
        System.out.println("coucou 3");
        
        IplImage dest = cvCreateImage(cvGetSize(conv), IPL_DEPTH_32F , conv.nChannels());
        cvCalcBackProject(channels, dest, hist);
        
        //System.out.println("coucou 4");
       // cvThreshold(dest, dest, 0.55, 1, CV_THRESH_BINARY) ;
		
*/
      /*
		for (int i = 0 ; i < 256 ; i++)
			for (int j = 0 ; j < 256 ; j++){
				for (int k = 0 ; k < 256 ; k++) {
					float f = opencv_legacy.cvQueryHistValue_3D(hist, i, j, k) ;
					if (f != 0.0)
						System.out.printf("Pixel %d %d %d %f\n", i ,j , k , f);
					
				}	
			}
		*/
        
       // cvThreshold(imgA, imgA ,  100, 255, CV_THRESH_BINARY_INV);
        
		//System.out.println("end");
        
        
        
        cvShowImage( "LKpyr_OpticalFlow", imgA );
        cvWaitKey(3) ;
        cvReleaseImage(grayImage);
       /* cvReleaseImage(conv);
        cvReleaseImage(dest);
        //cvReleaseImage(imgA);
       
        for (cpt = 0 ; cpt < dims ; cpt++) {
        	cvReleaseImage(chanels[cpt]) ;
        	cvReleaseImage(channels[cpt]) ;
        }*/
        
	}

	@Override
	public void setIImageResoning(IImageReasoning imgR) {
		// TODO Auto-generated method stub
		this.imgReasoning = imgR ;
	}

}
