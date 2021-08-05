package com.digital.receipt.common.enums;

import java.util.Arrays;
import java.util.List;

public enum QueryStatement {
	NONE("@NONE", "NONE"), AND("@AND", "AND"), WHERE("@WHERE", "WHERE"), IF("@IF", "IF");

	private String annotation;
	private String type;

	QueryStatement(String annotation, String type) {
		this.annotation = annotation;
		this.type = type;
	}

	public String annotation() {
		return annotation;
	}

	public String text() {
		return type;
	}

	/**
	 * Checks to see if the current enum type is a valid query statement. If it's
	 * not then it will return false
	 * 
	 * @return boolean if type is not equal to NONE
	 */
	public boolean isQueryStatement() {
		return type != "NONE";
	}

	/**
	 * Given a string, it will return the enum that the string contains. If the
	 * string does not contain any of the known annotations it will return NONE
	 * 
	 * @param str - String value to search
	 * @return QueryStatement enum that the string contains (If one exists)
	 */
	public static QueryStatement getEnumFromString(String str) {
		for (QueryStatement value : QueryStatement.values()) {
			if (str.contains(value.annotation()))
				return value;
		}
		return NONE;
	}
}
