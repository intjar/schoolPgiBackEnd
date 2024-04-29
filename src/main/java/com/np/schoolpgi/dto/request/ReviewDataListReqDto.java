package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.np.schoolpgi.constants.APPServiceCode;

import lombok.Data;

@Data
public class ReviewDataListReqDto {

	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer loginId;
	@NotEmpty(message = APPServiceCode.MUST_CONTAIN)
	private String inpPg;
	private String yearcode;
}
