package com.digital.receipt;

import com.digital.receipt.service.activeProfile.ActiveProfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DigitalReceiptReaderBackendApplication {

	public static void main(String[] args) {
		ActiveProfile activeProfile = new ActiveProfile();
		activeProfile.setPropertyFile();

		SpringApplication.run(DigitalReceiptReaderBackendApplication.class, args);
	}

}
