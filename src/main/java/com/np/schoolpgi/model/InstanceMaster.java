package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "instance_master")
@Entity
@Data
public class InstanceMaster {
	
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
       @Column(name="id")	
	   private Integer id;
       
       @Column(name="instance_name")
       private String instanceName;
 
       @Column(name="level_id")
       private Integer level;
       
       @Column(name="parent_instance_id")
       private Integer parentInstanceId;
       
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
	    
	    @Column(name="instance_code")
	    private Long instanceCode;
	    
}
