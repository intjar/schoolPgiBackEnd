package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.ToString;

@NamedStoredProcedureQuery(name = "get_level_master_data",procedureName = "get_level_master_data", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.INOUT, name = "_jsontext", type = String.class)
})
@Table(name = "level_master")
@Entity
@Data
@ToString
public class LevelMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "level_name")
	private String levelName;
	
	@Column(name = "level_code")
	private Integer levelCode;
	
	@Column(name = "parent_level_id")
	private Integer parentLevelId;
	
	@Column(name = "role_ids")
	private String roleIds;
	
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
	private String activeRole;
	
	
}
