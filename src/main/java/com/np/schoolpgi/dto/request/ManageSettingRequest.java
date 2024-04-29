package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.ToString;

@Data
@Validated
@ToString
public class ManageSettingRequest {

	@NotNull
	private Integer roleid;
	
	@NotNull
	private Integer rolecode;

	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public Integer getRolecode() {
		return rolecode;
	}

	public void setRolecode(Integer rolecode) {
		this.rolecode = rolecode;
	}

	
	
}
