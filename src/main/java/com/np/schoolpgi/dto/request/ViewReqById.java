package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotNull;

import com.np.schoolpgi.constants.APPServiceCode;

import lombok.Data;

@Data
public class ViewReqById {
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer id;
	
	private String yearcode;
}
