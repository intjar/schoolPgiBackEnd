package com.np.schoolpgi.util;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiError
{
//    @JsonIgnore
	private Boolean				success;
    private HttpStatus        status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime     timestamp;
    
    private String				errorMessage;
   
//    private String            debugMessage;
   
    private String            requestDesc;
    
   
//    public ApiError( HttpStatus status, Throwable ex, String requestDesc )
//    {
//        this.status = status;
//        this.errorMessage = "Unexpected error";
//        this.debugMessage = ex.getLocalizedMessage();
//        this.requestDesc = requestDesc;
//    }

//    public ApiError( HttpStatus status, String message, String requestDesc )
//    {
//        this.status = status;
//        this.errorMessage = message;
//        this.requestDesc = requestDesc;
//    }

//    public ApiError( HttpStatus status, String message, Throwable ex, String requestDesc )
//    {
//        this.status = status;
//        this.errorMessage = message;
//        this.debugMessage = ex.getLocalizedMessage();
//        this.requestDesc = requestDesc;
//    }

   
    
//	public ApiError(HttpStatus status, String message, String debugMessage,
//			String requestDesc) {
//		super();
//		this.status = status;
//		this.errorMessage = message;
//		this.debugMessage = debugMessage;
//		this.requestDesc = requestDesc;
//	}

	public ApiError(Boolean success, HttpStatus status, String message,
			String requestDesc) {
		super();
		this.success = success;
		this.status = status;
		this.errorMessage = message;
		this.requestDesc = requestDesc;
	}

}
