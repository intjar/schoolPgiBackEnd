package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "excel_signature")
@Entity
@Data
public class ExcelSignature {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="survey_id")
	private Integer surveyId;
	
	@Column(name="signature")
	private String signature;
	
	@Column(name="file_url")
	private String fileUrl;
	
	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	
}
