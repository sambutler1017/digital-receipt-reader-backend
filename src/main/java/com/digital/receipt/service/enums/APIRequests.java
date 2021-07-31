package com.digital.receipt.service.enums;

public enum APIRequests {
	GET("get"), POST("post"), UPDATE("update"), DELETE("delete");

	private String apiType;

	APIRequests(String type) {
		this.apiType = type;
	}

	public String text() {
		return apiType;
	}
}
