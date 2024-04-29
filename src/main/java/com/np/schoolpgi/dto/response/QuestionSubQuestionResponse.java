package com.np.schoolpgi.dto.response;

import java.util.Date;
import java.util.List;

import com.np.schoolpgi.model.DomainMaster;
import com.np.schoolpgi.model.QuestionTypeMaster;
import com.np.schoolpgi.model.SubDomain;
import com.np.schoolpgi.model.SubQuestion;

import lombok.Data;

@Data
public class QuestionSubQuestionResponse {
	
	
	private Long id;
	
	private String question;
	
	private String SNo;
//	@Column(name = "question_type_id")
//	private Integer questionTypeId;
	
	private Integer questionCode;
	
	private String options;
	
	private Boolean status;
	
	private Date createdAt;
	
	private Integer createdBy;
	
	private Date updatedAt;

	private Integer updatedBy;
	
	private List<SubQuestion> subQuestion;
	
	private QuestionTypeMaster questionTypeMaster;
	
	private DomainMaster domain;
	
	private SubDomain subDomain;
	
	private Boolean onlyNumeric;
	
	private String valueLogic;
	
	private String pointerLogic;
	
	private Double weightage;

}
