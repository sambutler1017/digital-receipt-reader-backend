package com.digital.receipt;

import com.digital.receipt.service.activeProfile.ActiveProfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The starter spring boot application for Digital Receipt Reader.
 * 
 * @author Sam Butler
 * @since July 1, 2021
 */
@SpringBootApplication
public class DigitalReceiptReaderBackendApplication {
	/**
	 * Main method to start the application.
	 * 
	 * @param args The arguments to be run witht he application, if any.
	 */
	public static void main(String[] args) {
		setPropertyFile();
		SpringApplication.run(DigitalReceiptReaderBackendApplication.class, args);
	}

	/**
	 * Method to set the property file for the running application. It wil either be
	 * a local instance or production instance.
	 */
	private static void setPropertyFile() {
		ActiveProfile activeProfile = new ActiveProfile();
		activeProfile.setPropertyFile();
	}
}
