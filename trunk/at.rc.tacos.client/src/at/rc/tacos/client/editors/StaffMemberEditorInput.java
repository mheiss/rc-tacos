package at.rc.tacos.client.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.rc.tacos.model.Login;
import at.rc.tacos.model.StaffMember;

public class StaffMemberEditorInput implements IEditorInput
{
    //properties
    private StaffMember staffMember;
    private Login loginInfo;
    private boolean isNew;
    
    /**
     * Default class constructor for the staff member editor.
     * @param staffMember the staffMember to edit
     * @param login the login information accociated with the staff member
     * @param isNew a flag to determine whether the member is new
     */
    public StaffMemberEditorInput(StaffMember staffMember,Login login,boolean isNew)
    {
        this.staffMember = staffMember;
        this.loginInfo = login;
        this.isNew = isNew;
    }
    
    @Override
    public boolean exists()
    {
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return staffMember.getLastName();
    }

    @Override
    public IPersistableElement getPersistable()
    {
        return null;
    }

    @Override
    public String getToolTipText()
    {
        return "Mitarbeiter: " +staffMember.getLastName() + " " + staffMember.getFirstName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class arg0)
    {
        return null;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other instanceof StaffMemberEditorInput)
        {
            StaffMemberEditorInput otherEditor = (StaffMemberEditorInput)other;
            if(staffMember.getLastName().equals(otherEditor.getName()))
                return true;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return staffMember.hashCode();
    }
    
    /**
     * Returns the staff member
     * @return the staff member
     */
    public StaffMember getStaffMember()
    {
    	return staffMember;
    }
    
    /**
     * Returns the login information accociated with the staff member
     * @return the login info
     */
    public Login getLoginInformation()
    {
    	return loginInfo;
    }
    
    /**
     * Returns whether or not the staff member is new or not.
     * @return true if the staff member is created new
     */
    public boolean isNew()
    {
    	return isNew;
    }
}
