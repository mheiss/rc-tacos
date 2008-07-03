package at.rc.tacos.server.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import at.rc.tacos.server.Activator;
import at.rc.tacos.server.ui.utils.CustomUI;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */
public class DbPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage 
{
	/**
	 * Default class constructor
	 */
	public DbPreferencesPage() 
	{
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() 
	{
		//add a header
		Label serverHeader = new Label(getFieldEditorParent(),SWT.NONE);
		serverHeader.setText("Datenbankeinstellungen des TACOS-Servers");
		serverHeader.setFont(CustomUI.PREFERENCE_FONT);
		adjustGridLayout();
		//the preferences field
		addField(new StringFieldEditor(PreferenceConstants.P_DB_DRIVER, "Datenbanktreiber",getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_DB_HOST, "Hostname des Servers", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_DB_USER, "Benutzername",getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_DB_PW, "Passwort",getFieldEditorParent()));
		
		//layout the label
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		data.heightHint = 30;
		serverHeader.setLayoutData(data);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
}