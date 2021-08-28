package com.digital.receipt.common.enums;

public enum QueryTag {
	NONE("@NONE", "NONE"), AND("@AND", "AND"), WHERE("@WHERE", "WHERE"), IF("@IF", "IF"), OR("@OR", "OR");

	private String annotation;
	private String type;

	QueryTag(String annotation, String type) {
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
	public boolean isQueryTag() {
		return type != "NONE";
	}

	/**
	 * Given a string, it will return the enum that the string contains. If the
	 * string does not contain any of the known annotations it will return NONE
	 * 
	 * @param str - String value to search
	 * @return QueryStatement enum that the string contains (If one exists)
	 */
	public static QueryTag getEnumFromString(String str) {
		for (QueryTag value : QueryTag.values()) {
			if (str.contains(value.annotation()))
				return value;
		}
		return NONE;
	}
}
