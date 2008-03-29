package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import at.rc.tacos.model.Competence;

public class CompetenceLabelProvider extends LabelProvider 
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
//        return ImageFactory.getInstance().getRegisteredImage("resource.competence");
    }

    /**
     * Returns the text to render.
     */
    @Override
    public String getText(Object object)
    {
    	Competence competence = (Competence)object;
        return competence.getCompetenceName();
    }
}