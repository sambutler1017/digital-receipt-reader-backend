package com.digital.receipt.service.enums;

public enum InsiteSqlTags {
	NAME("@NAME"), INCLUDE("@INCLUDE"), VALUE_ID(":");

	private String annotation;

	InsiteSqlTags(String annotation) {
		this.annotation = annotation;
	}

	public String text() {
		return annotation;
	}

	public boolean in(String str) {
		return str.contains(annotation);
	}
}
