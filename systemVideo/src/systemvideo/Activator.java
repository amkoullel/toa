package systemvideo;

import java.util.HashMap;
import java.util.Map;

import interfaces.IImageAcquisition;
import interfaces.IImageAnalysis;
import interfaces.IImagePublish;
import interfaces.IImageReasoning;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.jface.preference.FieldEditor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "systemVideo";

	// The shared instance
	private static Activator plugin;
	
	private Map<String , IConfigurationElement> mesConfiguration ; 
	private boolean setPreference  ; //avons nous choisit des preferences 
	
	/**
	 * The constructor
	 */
	public Activator() {
		mesConfiguration = new HashMap<String , IConfigurationElement> () ;
		setPreference = false ;
	}
	
	public IConfigurationElement getConfigurationElement (String s) {
		return mesConfiguration.get(s);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public void createFieldEditorsAcquisition(String extension , String label , PreferencePage p,  Composite fieldEditorParent) {
		
		IConfigurationElement [] elts = RegistryFactory.getRegistry().getConfigurationElementsFor(extension) ;
		String [][] namesValues = new String[elts.length][2] ;
		int cpt = 0 ;
		for (IConfigurationElement elt : elts) {
			String [] tab = new String [2] ;
			tab[0] =  elt.getAttribute("name") ;
			tab[1] =  elt.getAttribute("class") ;
			namesValues[cpt++] = tab ;
			mesConfiguration.put(tab[1],elt) ;
			System.out.println(tab[1]);
		}
		
		FieldEditor f =	new RadioGroupFieldEditor (extension , label , namesValues.length ,
									 namesValues , fieldEditorParent , true);
		p.add(f);
	}
	
	
	
	public void createFieldEditors(PreferencePage p, Composite fieldEditorParent) {
		// TODO Auto-generated method stub
		createFieldEditorsAcquisition("systemVideo.imageAcquisition", "Plugin acquisition d'images" , p ,fieldEditorParent );
		FieldEditor f =	new IntegerFieldEditor("Frame", "Une capture toutes les ... images", fieldEditorParent) ;
		p.add(f);
		
		
		createFieldEditorsAcquisition("systemVideo.imageAnalysis", "Plugin analyse d'images" , p ,fieldEditorParent );
		createFieldEditorsAcquisition("systemVideo.imageReasoning", "Plugin raisonnement" , p ,fieldEditorParent );
		createFieldEditorsAcquisition("systemVideo.imagePublish", "Plugin de publication" , p ,fieldEditorParent );
		
		f =	new StringFieldEditor("ProxyHost", "adresse du proxy ", fieldEditorParent) ;
		p.add(f);
			
		f =	new IntegerFieldEditor("ProxyPort", "port du proxy ", fieldEditorParent) ;
		p.add(f);
	}
	

	public IPreferenceStore getStore () {
		if (!setPreference)
			setPreference = true ;
		return getPreferenceStore();	
	}
	
	private String getString (IPreferenceStore store , String s) {
		if (setPreference)
			return store.getString(s) ;
		else
			return store.getDefaultString(s);
		
	}
	
	/**
	 * creation des differents objets
	 * @return return le plugin d'image d'acquisiton
	 * @throws Exception
	 */
	
	public IImageAcquisition createObjects() throws Exception {
		// TODO Auto-generated method stub

		IPreferenceStore store = Activator.getDefault().getPreferenceStore() ;
		
		String s = getString(store , "systemVideo.imageAcquisition") ;
		IConfigurationElement elt =  Activator.getDefault().getConfigurationElement(s) ;
		IImageAcquisition imgAcquisition = (IImageAcquisition) elt.createExecutableExtension("class");
		
		s = getString(store , "systemVideo.imageAnalysis") ;
		IImageAnalysis imgAnalyse = (IImageAnalysis) Activator.getDefault().getConfigurationElement(s).createExecutableExtension("class");
		
		s = getString(store , "systemVideo.imageReasoning") ;
		IImageReasoning imgReasoning = (IImageReasoning) Activator.getDefault().getConfigurationElement(s).createExecutableExtension("class");
			
		s = getString(store , "systemVideo.imagePublish") ;
		IImagePublish imgPublish = (IImagePublish) Activator.getDefault().getConfigurationElement(s).createExecutableExtension("class");
		
		
		s = getString(store , "ProxyHost") ;
		
		if (!s.equals("")) {
			imgPublish.setProxyHost(s);
			s = getString(store , "ProxyPort") ;
			imgPublish.setProxyPort(s) ;
		}
		
		imgReasoning.addIImagePublish(imgPublish);
		imgAnalyse.setIImageResoning(imgReasoning);
		imgAcquisition.setIImageAnalysis(imgAnalyse);
		
		return imgAcquisition ;
	}
	
}
