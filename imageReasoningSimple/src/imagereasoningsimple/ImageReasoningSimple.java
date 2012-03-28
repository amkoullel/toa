package imagereasoningsimple;

import java.util.ArrayList;

import interfaces.IImagePublish;
import interfaces.IImageReasoning;

public class ImageReasoningSimple implements IImageReasoning {
	private ArrayList<IImagePublish> publishers ;
	
	public ImageReasoningSimple() {
		// TODO Auto-generated constructor stub
		publishers = new ArrayList<IImagePublish> () ;
	}

	@Override
	public void reasonnig(ArrayList<Object> o) {
		// TODO Auto-generated method stub
		System.out.println("publier ") ; 
		for (IImagePublish p : publishers)
			p.publish("Rien", "Rien");
	}

	@Override
	public void addIImagePublish(IImagePublish imgP) {
		// ajoute un moyen de publication a ceux deja connu
		publishers.add(imgP) ;
	}
}
