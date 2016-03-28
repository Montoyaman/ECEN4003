package dictionaryattack;

/**
 * 	Hasher takes a string and hash algorithm and finds the
 * 	hash of the string using that hash algorithm.
 * @author CyParker
 */

import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;

public class Hasher{

	public String password;
	public String password_hash;
	
	/**
	 * Constructor
	 * 
	 * @param string   password string to store and hash
	 * @param hash_algorithm  hash algorithm to use
	 */
	public Hasher(String string, String hash_algorithm) {
	
		password = string;
		hash(hash_algorithm);
	}
	
	/**
	 * Hashes the password using the given hash_algorithm
	 * @param String hash_algorithm hash algorithm to use
	 */
	void hash(String hash_algorithm){
		
		MessageDigest hasher = null;
		
		try {
			hasher = MessageDigest.getInstance(hash_algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		
		//MessageDigest takes in a byte array
		hasher.update(password.getBytes());
		// Computes the hash and then converts the hashed byte array to a (lower case) string
		password_hash = DatatypeConverter.printHexBinary(hasher.digest()).toLowerCase();
	}
}