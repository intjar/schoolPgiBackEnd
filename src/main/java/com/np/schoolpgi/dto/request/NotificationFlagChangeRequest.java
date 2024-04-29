package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NotificationFlagChangeRequest {

	@NotNull
	private Integer id;
	@NotNull
	private Integer loggedInUserId;
	@NotNull
	private boolean flag;
	
}
