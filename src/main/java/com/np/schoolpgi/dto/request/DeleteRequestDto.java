package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotNull;

import com.np.schoolpgi.constants.APPServiceCode;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeleteRequestDto {
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Long id;
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer loggedInUserId;
}