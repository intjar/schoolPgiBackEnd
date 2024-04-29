package com.np.schoolpgi.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.np.schoolpgi.constants.APPServiceCode;

import lombok.Data;

@Data
public class ViewByListIds {

	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	@NotEmpty(message = APPServiceCode.MUST_CONTAIN)
	private List<Integer> listIds;
}
