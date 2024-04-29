package com.np.schoolpgi.dto.response;

import lombok.Data;

@Data
public class Error {
	private Boolean isError;
    private ResultInfo resultInfo;
	public Error(Boolean isError, ResultInfo resultInfo) {
		super();
		this.isError = isError;
		this.resultInfo = resultInfo;
	}
	public Error() {
	}
    
}
