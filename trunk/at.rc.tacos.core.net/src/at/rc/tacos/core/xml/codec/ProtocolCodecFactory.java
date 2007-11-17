package at.rc.tacos.core.xml.codec;

import java.util.HashMap;

/**
 * This class manages the decoders and the encoders to
 * serialize and unserialize a object.
 * @author Michael
 */
public class ProtocolCodecFactory
{
    //the shared instance
    private static ProtocolCodecFactory instance;
    
    //encoders
    private HashMap<String, MessageEncoder> encoders;
    private HashMap<String, MessageDecoder> decoders;
    
    /**
     * Default private class constructor
     */
    private ProtocolCodecFactory()
    {
        //set up the encoders and decoders
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
     */
    public MessageDecoder getDecoder(String id)
    {
        return decoders.get(id);
    }
    
    /**
     * Returns a encoder for this type of message
     * or null if no encoder is registered.
     * @param id the identification of the encoder to get.
     */
    public MessageEncoder getEncoder(String id)
    {
        return encoders.get(id);
    }
}
