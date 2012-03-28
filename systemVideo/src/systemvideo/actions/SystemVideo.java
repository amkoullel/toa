package systemvideo.actions;

import interfaces.IImageAcquisition;
import interfaces.IImageAnalysis;
import interfaces.IImagePublish;
import interfaces.IImageReasoning;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
//import org.eclipse.jface.dialogs.MessageDialog;


/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class SystemVideo implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;	
	private IImageAcquisition imgAcquisition ;
		
	/**
	 * The constructor.
	 */
	public SystemVideo() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
	this.createObjects () ;
	System.out.println("create");
		if (this.imgAcquisition != null)
			this.imgAcquisition.run() ;
		else
			System.out.println("Erreur");		
	}
	
	private void createObjects() {
		// TODO Auto-generated method stub
		this.imgAcquisition = systemvideo.Activator.getDefault().createImageAcquisiton() ;	
		IImageAnalysis	imgAnalyse = systemvideo.Activator.getDefault().createImageAnalysis();	
		IImageReasoning	imgReasoning = systemvideo.Activator.getDefault().createImageReasoning() ;
		IImagePublish	imgPublish =  systemvideo.Activator.getDefault().createImagePublish() ;
	
		imgReasoning.addIImagePublish(imgPublish) ;
		imgAnalyse.setIImageResoning(imgReasoning);	
		this.imgAcquisition.setIImageAnalysis(imgAnalyse);
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}