package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "event_log")
@Entity
@Data
public class EventLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "event_id")
	private Integer eventId;

	@Column(name = "event_name")
	private String eventName;

	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name="request_body")
	private String request;
	
	@Column(name="response_body")
	private String response;

	@Column(name = "created_at")
	private Date createdAt;

}
