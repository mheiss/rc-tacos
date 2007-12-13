package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import at.rc.tacos.model.StaffMember;

public class StaffComboLabelProvider implements ILabelProvider 
{

    @Override
    public Image getImage(Object object)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getText(Object object)
    {
        StaffMember member = (StaffMember)object;
        return member.getFirstName() + " " + member.getLastname();
    }

    @Override
    public void addListener(ILabelProviderListener object)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isLabelProperty(Object object, String arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener object)
    {
        // TODO Auto-generated method stub
        
    }

}
