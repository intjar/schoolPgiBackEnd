package com.np.schoolpgi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "question_master")
public class QuestionMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "question")
	private String question;
	
//	@Column(name = "question_type_id")
//	private Integer questionTypeId;
	
	@Column(name = "question_code")
	private Integer questionCode;
	
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
	
	@OneToMany(mappedBy = "questionMaster")
	@JsonIgnore
	private List<SubQuestion> subQuestion;
	
	@JoinColumn(name = "question_type_id", referencedColumnName = "id")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private QuestionTypeMaster questionTypeMaster;
	
	@Column(name = "only_numeric")
	private Boolean onlyNumeric;
	
	@Transient
	private Boolean isEditable;
	
	
	
}
