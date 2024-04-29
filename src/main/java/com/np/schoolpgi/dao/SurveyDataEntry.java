package com.np.schoolpgi.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "survey_data_entry")
@Data
public class SurveyDataEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "survey_id")
	private Long surveyId;

	@Column(name = "survey_name")
	private String surveyName;

	@Column(name = "year_assigned_on")
	private String yearAssignedOn;

	@Column(name = "valid_upto")
	private String validUpto;

	@Column(name = "survey_desc")
	private String surveyDesc;

	@Column(name = "domain_id")
	private Integer domainId;

	@Column(name = "domain_name")
	private String domainName;

	@Column(name = "sub_domain_id")
	private Integer subDomainId;

	@Column(name = "sub_domain_name")
	private String subDomainName;

	@Column(name = "question_id")
	private Long questionId;

	@Column(name = "sub_question_id")
	private Integer subQuestionId;

	@Column(name = "ans_options")
	private String ansOptions;

	@Column(name = "answer")
	private String answer;

	@Column(name = "answer_type_id")
	private Integer answerTypeId;

	@Column(name = "status")
	private String status;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "third_party")
	private Integer thirdParty;

}
