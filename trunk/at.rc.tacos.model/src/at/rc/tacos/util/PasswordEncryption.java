package at.rc.tacos.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * Provides methods to generate a hash value out of a given string.
 * The original password cannot be recovered. This is a implementation
 * of a one-way hash algorithm.
 * 
 * Code from: Devarticles.com -> Password Encryption
 * 
 * @author Michael
 */
public class PasswordEncryption
{
    //The instance of this class
    private static PasswordEncryption instance;

    /**
     * Default class constructor.
     */
    private PasswordEncryption()
    {
    }

    /**
     * Generates a hash value out of a given string and returns it.
     * The SHA-1 algorithm will be used to encrypt the password.
     * @param password the plain text password to generate the hash
     * @return the hash value for the given input
     */
    public synchronized String encrypt(String password)
    {
        try
        {
            //the object responsible for the encryption
            MessageDigest md = null;
            //get a instance of the SHA-1 algorithm
            md = MessageDigest.getInstance("SHA-1"); 
            //convert the password into bytes and 
            //update the message digest object
            md.update(password.getBytes("UTF-8")); 
            //encrypt the password
            byte raw[] = md.digest();
            //create a string out of the byte array
            String hash = (new BASE64Encoder()).encode(raw); 
            //return the hash value of the password
            return hash;
        }
        catch(NoSuchAlgorithmException e)
        {
            System.out.println("Algorithm SHA-1 is not supported");
            return null;
        }
        catch(UnsupportedEncodingException e)
        {
            System.out.println("UTF-8 encoding is not supported");
            return null;
        }
    }

    /**
     * Creates a new instance of this class or returns the 
     * previousely used instance.
     * @return a instance of the <code>SecurePassword</code> class.
     */
    public static synchronized PasswordEncryption getInstance() //step 1
    {
        //do we have a valid instance?
        if(instance == null)
            //create a new and return it
            return new PasswordEncryption();
        else    
            return instance;
    }
}
