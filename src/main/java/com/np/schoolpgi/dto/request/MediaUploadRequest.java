package com.np.schoolpgi.dto.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.np.schoolpgi.constants.APPServiceCode;

import lombok.Data;

@Data
public class MediaUploadRequest {
	
	
	private Long id;

	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer categoryId;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String categoryName;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String fileName;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String fileType;
	
	@NotNull
	@NotEmpty(message = APPServiceCode.MUST_CONTAIN)
	private byte[] file;
	
	private Date createdAt;
	
	private Integer createdBy;
	
	private Date updatedAt;
	
	private Integer updatedBy;
	
}
