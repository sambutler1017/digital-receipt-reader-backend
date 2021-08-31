package com.digital.receipt.service.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.digital.receipt.app.auth.client.domain.AuthPassword;

/**
 * Class used to hash passwords.
 * 
 * @author Seth Hancock
 * @since August 1, 2020
 */
public class PasswordUtil {

	/**
	 * Will generate a hash along with a random 10 digit long value. Adds extra
	 * layer of security to a user password.
	 * 
	 * @param pass The string to be hashed.
	 * @return {@link String} hash
	 * @throws NoSuchAlgorithmException
	 */
	public static AuthPassword hashPasswordWithSalt(String pass) throws NoSuchAlgorithmException {
		long generatedSalt = generatePasswordSalt();
		return new AuthPassword(hashPassword(appendSaltToPassword(pass, generatedSalt)), generatedSalt);
	}

	/**
	 * Will hash the given string with the appended salt value with SHA-256.
	 * 
	 * @param authPass {@link AuthPassword} object
	 * @return {@link String} hash
	 * @throws NoSuchAlgorithmException
	 */
	public static String hashPassword(AuthPassword authPass) throws NoSuchAlgorithmException {
		return hashPassword(appendSaltToPassword(authPass.getPassword(), authPass.getSalt()));
	}

	/**
	 * Will hash the given string with SHA-256.
	 * 
	 * @param pass The string to be hashed.
	 * @return {@link String} hash
	 * @throws NoSuchAlgorithmException
	 */
	public static String hashPassword(String pass) throws NoSuchAlgorithmException {
		byte[] hash = getSHA(pass);
		BigInteger number = new BigInteger(1, hash);

		StringBuilder hexString = new StringBuilder(number.toString(16));

		while (hexString.length() < 32) {
			hexString.insert(0, '0');
		}

		return hexString.toString();
	}

	/**
	 * Checks to see if the given string password matches the hashed password.
	 * 
	 * @param stringPass String password.
	 * @param hashPass   Hashed Password.
	 * @return {@link boolean} of the result of the match.
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean checkPassword(String stringPass, String hashPass) throws NoSuchAlgorithmException {
		return hashPassword(stringPass).equals(hashPass);
	}

	/**
	 * Checks to see if the given string password with the appended salt matches the
	 * hashed password.
	 * 
	 * @param stringPass String password.
	 * @param salt       The salt value to append to the string.
	 * @param hashPass   Hashed Password.
	 * @return {@link boolean} of the result of the match.
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean checkPassword(String stringPass, AuthPassword authPass) throws NoSuchAlgorithmException {
		return checkPassword(stringPass, appendSaltToPassword(authPass.getPassword(), authPass.getSalt()));
	}

	/**
	 * Appends the given string value and salt value together.
	 * 
	 * @param value Value to append salt value too.
	 * @param salt  The salt value to append to the string value.
	 * @return {@link String} of the appended result.
	 */
	public static String appendSaltToPassword(String value, long salt) {
		return String.format("%s%d", value, salt);
	}

	/**
	 * Generates a random 10 digit value that is used to append to strings for
	 * hashing and authentication.
	 * 
	 * @return {@link long} of the generated salt value.
	 */
	public static long generatePasswordSalt() {
		return (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
	}

	/**
	 * Gets the Sha instance for the given string.
	 * 
	 * @param input The string to turn into a bte Array
	 * @return {@link byte[]} of the input
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] getSHA(String input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		return md.digest(input.getBytes(StandardCharsets.UTF_8));
	}
}
