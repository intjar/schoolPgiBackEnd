package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "master_year")
@IdClass(YearMasterCompositeKey.class)
public class YearMaster {

	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Id
	@Column(name = "year_code", nullable = false)
	private String yearCode;

	@Id
	@Column(name = "year_id", nullable = false)
	private String yearId;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private Integer createdBy;

}
