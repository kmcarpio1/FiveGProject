package src.hashfunctions;

import java.math.BigInteger;
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException; 

/**
 * this static class is meant to hash a string with the MD5 hash technique, that always return a String of 128 bits 
 */
public class Hashing{
    /**
     * this method hash a string with the MD5 hash technique, that always return a String of 128 bits 
     * @param rawData : the rawData we want to hash
     * @return : the hashing String
     */
    public static String getMd5Hash(String rawData){
        try{

            MessageDigest md = MessageDigest.getInstance("MD5");  //this hash technique is always of 128 bits
            
            byte[] messageDigest = md.digest(rawData.getBytes());  
            
            BigInteger bigInt = new BigInteger(1, messageDigest);  

            String hash = bigInt.toString(16); //we transform it in hecadecimal, so the result is 32 caracters
            
            while (hash.length() < 32) { //but if the hexadecimal value of the hash starts with zeros, these zeros may be omitted in the BigInteger representation.
                hash = "0" + hash;
            }

            return hash;
        } catch(NoSuchAlgorithmException e){
            System.err.println("Error generating MD5 hash: " + e.getMessage());
            return ""; 
        }
    }
}