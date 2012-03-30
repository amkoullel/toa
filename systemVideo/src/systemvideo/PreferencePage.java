package systemvideo;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class PreferencePage extends org.eclipse.jface.preference.FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public PreferencePage() {
		// TODO Auto-generated constructor stub
		super(GRID);
	}
	
	/**
	 * 	initialise la page de preference
	 */
	@Override
	public void init(IWorkbench workbench) {	
		IPreferenceStore store =
			Activator.getDefault().getStore();
		setPreferenceStore(store);
	}
	
	public void add (FieldEditor f){
		addField(f) ;
	}


	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		Activator.getDefault().createFieldEditors(this, getFieldEditorParent()) ;	
	}

}
