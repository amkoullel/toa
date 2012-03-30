package imagereasoningsimple;

import java.util.ArrayList;
import java.util.List;

import interfaces.IImagePublish;
import interfaces.IImageReasoning;

public class ImageReasoningSimple implements IImageReasoning {
	private List<IImagePublish> publishers ;
	
	public ImageReasoningSimple() {
		// TODO Auto-generated constructor stub
		publishers = new ArrayList<IImagePublish> () ;	
	}

	@Override
	public void reasonnig(List<Object> o) {
		// TODO Auto-generated method stub
		//System.out.println("publier ") ; 
		if (o.size() > 1)
			for (IImagePublish p : publishers)
				p.publish("Visage", "Nous avons plus de deux visages");
	}

	@Override
	public void addIImagePublish(IImagePublish imgP) {
		// ajoute un moyen de publication a ceux deja connu
		publishers.add(imgP) ;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		for (IImagePublish p : publishers)
			p.init() ;
	}
}
