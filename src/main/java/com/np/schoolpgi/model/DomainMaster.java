package com.np.schoolpgi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "domain_master")
public class DomainMaster implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	
	@Column(name = "domain_name")
	private String domainName;
	
	@Column(name = "domain_code")
	private Integer domainCode;
	
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
	
	@OneToMany()
	@JsonIgnore
	private List<SubDomain> subDomain;
	
	@Transient
	private Boolean isEditable;
}
