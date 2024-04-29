package com.np.schoolpgi.dto.response;

import lombok.Data;

@Data
public class SurvMapSubQuestResponse {
	private Long subQuestId;
	private String subQuestName;
	private Integer subQuestTypeId;
	private String subQuestType;
	private Integer subQuestCode;
	private Boolean subQuestStatus;
}
