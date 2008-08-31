package at.rc.tacos.client.view;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.jface.internal.databinding.provisional.swt.AbstractSWTObservableValue;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;

public class MyImageLabelObserver extends AbstractSWTObservableValue
{
	//properties
	private final Label label;
	
	/**
	 * Default class constructor.
	 * @param label the label to observe
	 */
	public MyImageLabelObserver(Label label)
	{
		super(label);
		this.label = label;
	}
	
	/**
	 * Sets the value for this label.<br>
	 * Not that the value must be an image.
	 * @param newImage the image to set
	 */
	@Override
	public void doSetValue(final Object newImage) 
	{
		Image oldImage = label.getImage();
		label.setImage((Image)newImage);
		fireValueChange(Diffs.createValueDiff(oldImage, newImage));
	}

	/**
	 * Returns the acutal image for this label.
	 * @return the image displayed
	 */
	@Override
	protected Object doGetValue() 
	{
		return label.getImage();
	}

	/**
	 * Returns the type of the acutal displayed value.
	 * @return the content type of the label
	 */
	@Override
	public Object getValueType() 
	{
		return Image.class;
	}
}
