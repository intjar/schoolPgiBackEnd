package com.np.schoolpgi.enums;

public enum StatusCodes {
	INACTIVE(0, "Inactive"), ACTIVE(1, "Active"), DELETED(2, "Deleted"), CREATED(3, "Created"), EXPIRED(4, "Expired"),
	COMPLETED(5, "Completed");

	int code;
	String value;

	private StatusCodes(int inCode, String inValue) {
		this.code = inCode;
		this.value = inValue;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static StatusCodes getStatusByCode(int inCode) {
		for (StatusCodes status : StatusCodes.values()) {
			if (status.getCode() == inCode) {
				return status;
			}
		}
		return null;
	}
}
