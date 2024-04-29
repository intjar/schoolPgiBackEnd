package com.np.schoolpgi.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.constants.Regex;
import com.np.schoolpgi.model.QuestionMaster;
import com.np.schoolpgi.model.QuestionTypeMaster;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SubQuestionRequest {

	private Long id;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	@Size(min = 3, max = 500, message = APPServiceCode.QUESTION_LENGTH_MESSAGE)
	@Pattern(regexp = Regex.QUESTION_VALIDATION, message = APPServiceCode.QUESTION_FORMAT_MESSAGE)
	private String subQuestion;

	private QuestionTypeMaster subQuestionTypeMaster;

	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	@Min(value = 1, message = APPServiceCode.CODE_LENGTH_MESSAGE)
	@Max(value = 999999, message = APPServiceCode.CODE_LENGTH_MESSAGE)
	private Integer subQuestionCode;

	@Size(max = 1000, message = APPServiceCode.OPTIONS_LENGTH_MESSAGE)
	private String options;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Boolean status;

	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer loggedInUserId;
	
	private QuestionMaster questionMaster;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Boolean onlyNumeric;
}
