package com.digital.receipt.service.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class used to hash passwords.
 * 
 * @author Seth Hancock
 * @since August 1, 2020
 */
public class PasswordHash {

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
}
