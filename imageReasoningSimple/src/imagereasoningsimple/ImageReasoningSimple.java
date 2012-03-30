package imagereasoningsimple;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import interfaces.IImagePublish;
import interfaces.IImageReasoning;

public class ImageReasoningSimple implements IImageReasoning {
	private List<IImagePublish> publishers ;
	private int seuil ;
	
	public ImageReasoningSimple() {
		// TODO Auto-generated constructor stub
		publishers = new ArrayList<IImagePublish> () ;	
	}

	@Override
	public void reasonnig(List<Object> o) {
		// TODO Auto-generated method stub
		//System.out.println("publier ") ; 
		if (o.size() > seuil)
			for (IImagePublish p : publishers)
				p.publish("Visage", "Nous avons plus de " +seuil +" visages");
	}

	@Override
	public void addIImagePublish(IImagePublish imgP) {
		// ajoute un moyen de publication a ceux deja connu
		publishers.add(imgP) ;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		String val = JOptionPane.showInputDialog(null, "Nombre seuil de visage", "Pour la publication", JOptionPane.QUESTION_MESSAGE) ;
		if ( val== null || val.equals(""))
			throw new RuntimeException("Mauvais acces token");
       
		seuil = Integer.decode(val) ;
		if (seuil <= 0)
			throw new RuntimeException("Erreur saisie de valeur (pas de valeur nul ou negative)") ;
	    
		for (IImagePublish p : publishers)
			p.init() ;
	}
}
