package imageacquistionvideo;

import java.awt.image.BufferedImage;

import com.xuggle.xuggler.demos.VideoImage;

import interfaces.IImageAnalysis;
import interfaces.IImageReasoning;

public class TestAnalyse implements IImageAnalysis {
	
	public TestAnalyse () {
		openJavaWindow();
	}

	@Override
	public void analyse(BufferedImage img) {
		// TODO Auto-generated method stub
		if (img == null)
			closeJavaWindow();
		else
			updateJavaWindow(img);
	}
	
	
	private static VideoImage mScreen = null;

	  private static void updateJavaWindow(BufferedImage javaImage)
	  {
	    mScreen.setImage(javaImage);
	  }

	  /**
	   * Opens a Swing window on screen.
	   */
	  private static void openJavaWindow()
	  {
	    mScreen = new VideoImage();
	  }

	  /**
	   * Forces the swing thread to terminate; I'm sure there is a right
	   * way to do this in swing, but this works too.
	   */
	  private static void closeJavaWindow()
	  {
	    System.exit(0);
	  }

	@Override
	public void setIImageResoning(IImageReasoning imgR) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
