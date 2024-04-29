package com.np.schoolpgi.dto.response;

import java.util.List;

import com.np.schoolpgi.model.LinkList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManageSettingResponse {

	private Boolean success;
	private String message;
	private List<LinkList> result;

	public ManageSettingResponse(Boolean success, String message, List<LinkList> result) {
		super();
		this.success = success;
		this.message = message;
		this.result = result;
	}

}
