package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "event_list")
@Entity
@Data
public class EventMaster {
	
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
       @Column(name="id")	
	   private Integer id;
	   	
	   @Column(name="event_name")
	   private String eventName;
	   
//	   @Column(name="event_link")
//	   private String eventLink;
	   
	    
	   
	    @Column(name = "created_at")
	    private Date createdAt;
	    
	    @Column(name = "created_by")
	    private Integer createdBy;
	    
	    @Column(name = "updated_at")
	    private Date updatedAt;
	    
	    @Column(name = "updated_by")
	    private Integer updatedBy;

}
