package systemvideo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class PreferencePage extends org.eclipse.jface.preference.PreferencePage
		implements IWorkbenchPreferencePage {

	public PreferencePage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		 Composite composite = new Composite(parent, SWT.NONE);
         org.eclipse.swt.graphics.Color jaune = parent.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
         composite.setBackground(jaune);
         return composite;
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
	}

}
