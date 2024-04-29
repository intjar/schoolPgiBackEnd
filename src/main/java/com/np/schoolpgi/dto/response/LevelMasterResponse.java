package com.np.schoolpgi.dto.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LevelMasterResponse {
	private Integer id;
	private String level_name;
	private Integer level_code;
	private Integer parent_level_id;
	private String parent_level_name;
	private String role_ids;
	private String role_ids_name;
}
