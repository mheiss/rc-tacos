package at.rc.tacos.client.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.rc.tacos.model.StaffMember;

public class StaffMemberEditorInput implements IEditorInput
{
    //properties
    private StaffMember staffMember;
    
    /**
     * Default class constructor
     */
    public StaffMemberEditorInput(StaffMember staffMember)
    {
        this.staffMember = staffMember;
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
}
