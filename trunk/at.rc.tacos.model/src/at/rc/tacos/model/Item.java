package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * A simple itemen that has a name
 * @author Michael
 */
public class Item extends AbstractMessage
{   
    //properties
    private String name = null;

    /**
     * Default constructor
     */
    public Item() 
    { 
        super("item");
    }

    /**
     * Default constructor specifying the name of the item
     * @param name the name of the item
     */
    public Item(String name)
    {
        this();
        this.name = name;
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
     */
    public void setName(String name) 
    {
        this.name = name;
    }
}
