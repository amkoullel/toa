package systemvideo;

import interfaces.IImageAcquisition;
import interfaces.IImageAnalysis;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.jface.resource.ImageDescriptor;
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
	
	/**
	 * The constructor
	 */
	public Activator() {
	}
	
	public IImageAcquisition createImageAcquisiton () {
		for (IConfigurationElement elt : RegistryFactory.getRegistry().getConfigurationElementsFor("systemVideo.imageAcquisition")) {
			//System.out.println(elt.getAttribute("name"));
			String name = elt.getAttribute("name").toLowerCase() ;
			if (!name.equals("camera")) {
				try {
					return (IImageAcquisition) elt.createExecutableExtension("class");
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null ;
	}
	
	public IImageAnalysis createImageAnalysis() {
		// TODO Auto-generated method stub
		for (IConfigurationElement elt : RegistryFactory.getRegistry().getConfigurationElementsFor("systemVideo.imageAnalysis")) {
			//System.out.println(elt.getAttribute("name"));
			try {
				return (IImageAnalysis) elt.createExecutableExtension("class");
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
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
}
