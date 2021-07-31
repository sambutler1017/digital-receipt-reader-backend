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

	public static String hashPassword(String pass) throws NoSuchAlgorithmException {
		byte[] hash = getSHA(pass);
		BigInteger number = new BigInteger(1, hash);

		StringBuilder hexString = new StringBuilder(number.toString(16));

		while (hexString.length() < 32) {
			hexString.insert(0, '0');
		}

		return hexString.toString();
	}

	private static byte[] getSHA(String input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		return md.digest(input.getBytes(StandardCharsets.UTF_8));

	}

	public static boolean checkPassword(String stringPass, String hashPass) throws NoSuchAlgorithmException {
		return hashPassword(stringPass).equals(hashPass);
	}
}
