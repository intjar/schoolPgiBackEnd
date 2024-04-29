package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ViewSurMapSubQuestReq {
	@NotNull
	private Integer surveyId;
	@NotNull
	private Integer domainId;
	private Integer subDomainId;
	@NotNull
	private Long questionId;
}
