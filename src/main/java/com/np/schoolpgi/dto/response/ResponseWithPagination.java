package com.np.schoolpgi.dto.response;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ResponseWithPagination {
	private Boolean success;
	private HttpStatus httpStatus;
	private String messageCode;
	private String message;
	private Object result;
	private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private String errorCode;
    private String errorMessage;
}
