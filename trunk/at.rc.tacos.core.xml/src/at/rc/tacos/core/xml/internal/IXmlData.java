package at.rc.tacos.core.xml.internal;

import java.util.*;
import at.rc.tacos.client.model.*;

/**
 * The needed methods by the parser
 * @author Michael
 */
public interface IXmlData
{
    public Vector<Item> parseItemData(String xmlInput);
}
