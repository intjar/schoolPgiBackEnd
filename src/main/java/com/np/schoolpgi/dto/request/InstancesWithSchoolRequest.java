package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotNull;

import com.np.schoolpgi.constants.APPServiceCode;

import lombok.Data;

@Data
public class InstancesWithSchoolRequest {

	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Boolean withSchool; 
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Boolean isDropDown;
}
