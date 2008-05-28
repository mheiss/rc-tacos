package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import at.rc.tacos.model.SickPerson;

public class SickPersonLabelProvider extends LabelProvider 
{
	String sickPerson;
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
    	SickPerson person = (SickPerson)object;
    	sickPerson = person.getLastName();
    	if(person.getFirstName() != null)
    		sickPerson = sickPerson + " " +person.getFirstName();
    	if(person.getStreetname() != null)
    		sickPerson = sickPerson + " " +person.getStreetname();
    	if(person.getCityname() != null)
    		sickPerson = sickPerson + " " +person.getCityname();
    	if(person.getSVNR() != null)
    		sickPerson = sickPerson + " " +person.getSVNR();
    	if(person.getNotes() != null)
    		sickPerson = sickPerson + " " +person.getNotes();
    	return sickPerson;
    }
}