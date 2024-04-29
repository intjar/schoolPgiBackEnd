package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "sub_domain")
public class SubDomain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne()
	@JoinColumn(name = "domain_id", referencedColumnName = "id")
	private DomainMaster domainMaster;

	@Column(name = "sub_domain_name")
	private String subDomainName;
		
	@Column(name = "sub_domain_code")
	private Integer subDomainCode;
	
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
	
	@Transient
	private Boolean isEditable;
}
