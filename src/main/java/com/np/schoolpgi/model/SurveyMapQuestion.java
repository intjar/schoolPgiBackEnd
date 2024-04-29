package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "survey_map_question")
@Entity
@Data
public class SurveyMapQuestion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "survey_id", referencedColumnName = "id")
	private SurveyMaster surveyMaster;

	@ManyToOne()
	@JoinColumn(name = "domain_id", referencedColumnName = "id")
	private DomainMaster domainMaster;

	@ManyToOne()
	@JoinColumn(name = "sub_domain_id", referencedColumnName = "id")
	private SubDomain subDomain;

	@ManyToOne()
	@JoinColumn(name = "question_id", referencedColumnName = "id")
	private QuestionMaster questionMaster;

	@Column(name = "sub_question_ids")
	private String subQuestionIds;

	@Column(name = "value_logic")
	private String valueLogic;

	@Column(name = "pointer_logic")
	private String pointerLogic;

	@Column(name = "weightage")
	private Double weightage;

	@Column(name = "is_mandatory")
	private Integer isMandatory;

	@Column(name = "is_third_party")
	private Integer isThirdParty;
	
	@ManyToOne()
	@JoinColumn(name = "data_source_id", referencedColumnName = "id")
	private DataSource dataSource;

	@Column(name = "is_deleted")
	private Integer isDeleted;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "order_id")
	private Integer order;
	
	@Column(name = "sno")
	private String sNo;

}
