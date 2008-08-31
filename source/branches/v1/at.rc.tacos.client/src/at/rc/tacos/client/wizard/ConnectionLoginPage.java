package at.rc.tacos.client.wizard;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.wizard.WizardPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ConnectionLoginPage extends WizardPage
{
	//properties
	private Composite container;
	private ConnectionWizard wizard;
	//the components
	private StringFieldEditor username;
	private StringFieldEditor password;
	
	/**
	 * Default class construcotr
	 */
	public ConnectionLoginPage(ConnectionWizard wizard)
	{
		super("");
		setWizard(wizard);
		setTitle("Anmelden");
		setDescription("Bitte geben Sie ihren Benutzername und ihr Kennwort ein um sich am Server anzumelden");
		this.wizard = wizard;
	}

	/**
	 * Callback method to create the page content and initialize it
	 */
	@Override
	public void createControl(Composite parent) 
	{
		//the parent
		container = new Composite(parent, SWT.NULL);
		//the layout
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		// the label and the input field for the username
		username = new StringFieldEditor("username", "Benutzername: ",60, container) 
		{			
			@Override
			protected boolean doCheckState() 
			{
				return true;
			}
			
			@Override
			protected void valueChanged() 
			{
				super.valueChanged();
				isPageComplete();
				if (getWizard() != null) {
					getWizard().getContainer().updateButtons();
				}
			}
		};
		username.setEmptyStringAllowed(false);
		//the password field
		password = new StringFieldEditor("password", "Passwort: ",60, container)
		{
			@Override
			protected boolean doCheckState() 
			{
				return true;
			}
			
			@Override
			protected void valueChanged() 
			{
				super.valueChanged();
				isPageComplete();
				if (getWizard() != null) {
					getWizard().getContainer().updateButtons();
				}
			}
		};
		password.getTextControl(container).setEchoChar('*');
		password.setEmptyStringAllowed(false);
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}
	
	/**
	 * Returns the top widget of the application.
	 * @return the top widget
	 */
	@Override
	public Control getControl() 
	{
		return container;
	}
	
	/**
	 * Returns wheter or not the page is completed.
	 * This method checks all needed values and returns the status.
	 * @return true if the page is completed
	 */
	@Override
	public boolean isPageComplete() 
	{
		String username = getUsername();
		String password = getPassword();
		//check username
		if (username.trim().isEmpty())
		{
			setErrorMessage("Bitte geben sie einen Benutzernamen ein.");
			return false;
		}
		if (password.trim().isEmpty()) 
		{
			setErrorMessage("Bitte geben sie ein Passwort ein");
			return false;
		}
		setErrorMessage(null);
		//save the data
		wizard.setLoginData(username, password);
		//we have eyerything
		return true;
	}
	
	/**
	 * Returns the entered username from the editor.
	 * @return the username
	 */
	public String getUsername()
	{
		return username.getStringValue();
	}
	
	/**
	 * Returns the entered password from the editor.
	 * @return the password
	 */
	public String getPassword()
	{
		return password.getStringValue();
	}
}
