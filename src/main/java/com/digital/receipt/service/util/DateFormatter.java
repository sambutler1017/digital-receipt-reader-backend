package com.digital.receipt.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class used to format dates to a common format.
 * 
 * @author Seth Hancock
 * @since August 1, 2020
 */
public class DateFormatter {

	private static SimpleDateFormat MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
	private static SimpleDateFormat textDate = new SimpleDateFormat("MMMMM d, yyyy");
	private static SimpleDateFormat textDateShort = new SimpleDateFormat("MMM d, yyyy");

	public static String formatDate(Date dt) {
		return MMDDYYYY.format(dt);
	}

	public static String formatDateText(Date dt) {
		return textDate.format(dt);
	}

	public static String formatDateTextShort(Date dt) {
		return textDateShort.format(dt);
	}
}
