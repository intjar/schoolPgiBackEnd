package com.np.schoolpgi.dto.response;

import lombok.Data;

@Data
public class InstanceResponse {
	
	private Integer id;
	private String level;
	private Integer levelId;
	private String instance;
	private Long instanceCode;
	private String parentInstance="NA";
	private Integer parentInstanceId;
	private String parentLevel="NA";
	private Integer parentLevelId;
	private Boolean status;
	
	
}
