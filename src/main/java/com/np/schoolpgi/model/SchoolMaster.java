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

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "school_master")
public class SchoolMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne()
	@JoinColumn(name = "level_id", referencedColumnName = "id")
	private LevelMaster levelMaster;
	
	@Column(name = "block_level_instance_id")
	private Integer blockLevelInstanceId;
	
	@ManyToOne()
	@JoinColumn(name = "instance_id", referencedColumnName = "id")
	private InstanceMaster instanceMaster;
	
	@Column(name = "school_name")
	private String schoolName;
	
	@Column(name = "udise_code")
	private String udiseCode;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
}
