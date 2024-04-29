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

@Entity
@Table(    name = "user_details"
)
@Data
public class User {
    
    @Id
    @Column(name = "u_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId; 
    
  
    @Column(name = "name")
    private String name;
    
    @Column(name = "phone_no")
    private Long phoneNo;
    
    @Column(name = "email") 
    private String email;
    
   
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "instance_id", referencedColumnName = "id")
    private InstanceMaster instanceId;
    
    
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "level_id", referencedColumnName = "id")
    private LevelMaster levelMaster;
    

    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "role_id", referencedColumnName = "id")
    private Roles roleId;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @Column(name = "created_by")
    private Integer createdBy;
    
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @Column(name = "updated_by")
    private Integer updatedBy;
    
    @Column(name = "status")
    private Boolean status;
    
}