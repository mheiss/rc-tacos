package at.rc.tacos.factory;

import java.util.HashMap;
import java.util.Iterator;

import at.rc.tacos.codec.MessageDecoder;
import at.rc.tacos.codec.MessageEncoder;

/**
 * This class manages the decoders and the encoders to
 * serialize and unserialize a object.
 * @author Michael
 */
public class ProtocolCodecFactory
{
    private static ProtocolCodecFactory instance;
    
    //encoders and decoders
    private HashMap<String, MessageEncoder> encoders;
    private HashMap<String, MessageDecoder> decoders;
    
    /**
     * Default private class constructor
     */
    private ProtocolCodecFactory()
    {
        encoders = new HashMap<String, MessageEncoder>();
        decoders = new HashMap<String, MessageDecoder>();
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static ProtocolCodecFactory getDefault()
    {
        if(instance == null)
            instance = new ProtocolCodecFactory();
        return instance;
    }
    
    /**
     * Registers a decoder class for the given identification string.
     * @param id the identification for the decoder
     * @param decoder the class used to decode the message
     */
    public void registerDecoder(String id,MessageDecoder decoder)
    {
        decoders.put(id, decoder);
    }
    
    /**
     * Registers a encoder class for the given identification string.
     * @param id the identification for the encoder
     * @param encoder the encoder class to encode the message
     */
    public void registerEncoder(String id,MessageEncoder encoder)
    {
        encoders.put(id,encoder);
    }
    
    /**
     * Returns a decoder for this type of message
     * or null if no encoder registered.
     * @param id the identification of the decoder to get.
     * @return the decoder for this type of message
     */
    public MessageDecoder getDecoder(String id)
    {
        return decoders.get(id);
    }
    
    /**
     * Returns a encoder for this type of message
     * or null if no encoder is registered.
     * @param id the identification of the encoder to get.
     * @return the encoder for this type of message
     */
    public MessageEncoder getEncoder(String id)
    {
        return encoders.get(id);
    }
    
    /**
     * Prints out the available encoders to the console
     */
    public void printEncoder()
    {
    	Iterator<String> iter = encoders.keySet().iterator();
    	while(iter.hasNext())
    		System.out.println(iter.next());
    }
    
    /**
     * Convenience method to check if this type of
     * message has a decoder or not.
     * @param id the message to check for decoders
     * @return true if a decoder was found, otherwise false
     */
    public boolean hasDecoder(String id)
    {
        if (decoders.get(id) != null)
            return true;
        return false;
    }
    
    /**
     * Convenience method to check if this type of
     * message has a encoder or not.
     * @param id the message to check for enocders
     * @return true if a encoder was found, otherwise false
     */
    public boolean hasEncoders(String id)
    {
        if (encoders.get(id) != null)
            return true;
        return false;
    }
}
