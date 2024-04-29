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
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "sub_question_master")
public class SubQuestion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "sub_question")
	private String subQuestion;
	
//	@Column(name = "sub_question_type_id")
//	private Integer subQuestionTypeId;
	
	@Column(name = "sub_question_code")
	private Integer subQuestionCode;
	
	@Column(name = "options")
	private String options;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "question_id", referencedColumnName = "id")
	private QuestionMaster questionMaster;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sub_question_type_id", referencedColumnName = "id")
	private QuestionTypeMaster questionTypeMaster;
	
	@Column(name = "only_numeric")
	private Boolean onlyNumeric;
	
}
