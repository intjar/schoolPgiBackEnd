package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Validated
public class DeleteUpdateInstanceRequest {

	@NotNull
	private Integer parentInstanceId;
	
	@NotNull
	private Integer levelId;
	
	@NotEmpty
	private String instanceName;
	
	@NotNull
	private Long instanceCode;
	
	@NotNull
	private Boolean status;
	
	@NotNull
	private Integer loggedInUserId;
	
	@NotNull
	private Integer id;
}
