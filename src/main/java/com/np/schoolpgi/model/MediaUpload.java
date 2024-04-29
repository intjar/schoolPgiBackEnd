package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class MediaUpload {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "category_id")
	private Integer categoryId;
	
	@Column(name = "category_name")
	private String categoryName;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "file_type")
	private String fileType;
	
	@Column(name = "file_path")
	private String filePath;
	
	@Column(name = "created_at")
	private Date createdAt;
	
//	@Column(name = "created_by")
//	private Integer createdBy;
	
	@JoinColumn(name = "created_by", referencedColumnName = "u_id")
	@OneToOne()
	User user;
	
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
}
