package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "survey_notification")
public class SurveyNotification {

	@Id
	private Long id;
	@Column(name = "survey_id")
	private Integer surveyId;
	
	@Column(name = "year_code")
	private String yearCode;
	
	@Column(name = "survey_name")
	private String surveyName;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "created_by")
	private Long createdBy;
	
	@Column(name = "modified_at")
	private Date modifiedAt;
	
	@Column(name = "modified_by")
	private Long modifiedBy; 
	
}
