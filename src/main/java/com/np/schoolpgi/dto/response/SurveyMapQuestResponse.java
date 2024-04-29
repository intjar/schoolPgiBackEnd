package com.np.schoolpgi.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SurveyMapQuestResponse {
	private Integer id;
	private Integer surveyId;
	private Integer domainId;
	private String domainName;
	private Integer subDomainId;
	private String subDomainName;
	private Long questionId;
	private String question;
	private String questionType;
	private Integer questionCode;
	private Boolean questionStatus;
	private String subQuestionIds;
	private List<String> subQuestionName=new ArrayList<>();
	private List<Integer> subQuestionCode=new ArrayList<>();
	private String valueLogic;
	private String pointerLogic;
	private Double weightage;
	private Integer isMandatory;
	private Integer isThirdParty;
	private Integer dataSourceId;
	private String dataSourceName;
	private Integer isDeleted;
	private Integer order;
	private String sNo;
}
