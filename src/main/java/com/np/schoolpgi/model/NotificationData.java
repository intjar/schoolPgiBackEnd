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

@Table(name = "notification_data")
@Entity
@Data
public class NotificationData {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
    
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "reciever", referencedColumnName = "u_id")
	private User toWhom;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sender", referencedColumnName = "u_id")
	private User from;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "link_id", referencedColumnName = "id")
	private LinkList linkId;
	
	@Column(name="description")
	private String description;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "flag")
	private Boolean flag;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
}
