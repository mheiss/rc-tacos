package at.rc.tacos.client.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneEditorInput implements IEditorInput
{
    //properties
    private MobilePhoneDetail mobilePhone;
    
    /**
     * Default class constructor
     */
    public MobilePhoneEditorInput(MobilePhoneDetail mobilePhone)
    {
        this.mobilePhone = mobilePhone;
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
        return mobilePhone.getMobilePhoneName();
    }

    @Override
    public IPersistableElement getPersistable()
    {
        return null;
    }

    @Override
    public String getToolTipText()
    {
        return "Mobiltelefon: " +mobilePhone.getMobilePhoneName();
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
        if (other instanceof MobilePhoneEditorInput)
        {
            MobilePhoneEditorInput otherEditor = (MobilePhoneEditorInput)other;
            if(mobilePhone.getMobilePhoneName().equals(otherEditor.getName()))
                return true;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return mobilePhone.hashCode();
    }
}
