package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * A simple item that has a name<br>
 * This object is only for testing purpose.
 * @author Michael
 */
public class Item extends AbstractMessage
{   
    //unique identification string
    public final static String ID = "item";

    //properties
    private String name = null;

    /**
     * Default constructor
     */
    public Item() 
    { 
        super(ID);
    }

    /**
     * Default constructor specifying the name of the item
     * @param name the name of the item
     */
    public Item(String name)
    {
        this();
        setName(name);
    }

    //METHODS
    /**
     * Returns a string based description of the object
     * @return the description of the object
     */
    @Override
    public String toString()
    {
        return name;
    }

    //GETTERS AND SETTERS   
    /**
     * Returns the name of the item
     * @return the id
     */
    public String getName() 
    {
        return name;
    }

    /**
     * Sets the name of the item.
     * @param id the id to set
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setName(String name) 
    {
        if(name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("The name cannot be null or empty");
        this.name = name;
    }
}
