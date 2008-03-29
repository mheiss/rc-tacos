package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneLabelProvider  extends LabelProvider 
{
    /**
     * Returns the image to use for this element.
     * @param object the object to get the image for
     * @return the image to use
     */
    @Override
    public Image getImage(Object object)
    {
        return null;
    }

    /**
     * Returns the text to render.
     */
    @Override
    public String getText(Object object)
    {
        MobilePhoneDetail phone = (MobilePhoneDetail)object;
        return phone.getMobilePhoneName() + "-"+phone.getMobilePhoneNumber();
    }
}


