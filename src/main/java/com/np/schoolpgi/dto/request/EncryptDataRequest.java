package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.ToString;

@Data
@Validated
@ToString
public class EncryptDataRequest {
	
	@NotNull
	private String data;
	
}
