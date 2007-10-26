package at.rc.tacos.client.model;

/**
 * A simple itemen that has a name
 * @author Michael
 */
public class Item
{
    private String id = null;
    
    /**
     * Default constructor
     */
    public Item() { }
    
    /**
     * Default constructor specifying the name of the item
     * @param id the name of the item
     */
    public Item(String id)
    {
        this.id = id;
    }
    
    //METHODS
    /**
     * Serialize the content to xml
     */
    public String toXML()
    {
        return "<item>"+id+"<item>";
    }
    
    //GETTERS AND SETTERS   
    /**
     * @return the id
     */
    public String getId() 
    {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) 
    {
        this.id = id;
    }
}
