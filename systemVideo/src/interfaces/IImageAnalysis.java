package interfaces;

import java.awt.image.BufferedImage;

public interface IImageAnalysis {
	public void analyse (BufferedImage img);
	public void init ();
	public void setIImageResoning (IImageReasoning imgR);
}
