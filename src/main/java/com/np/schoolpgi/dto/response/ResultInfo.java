package com.np.schoolpgi.dto.response;

import lombok.Data;

@Data
public class ResultInfo {
	private Object resultCode;
    private Object resultMessage;
	public ResultInfo(Object resultCode, Object resultMessage) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
	public ResultInfo() {
	}
    
}