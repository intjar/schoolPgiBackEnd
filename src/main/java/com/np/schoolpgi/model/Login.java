package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "login")
@Data
@ToString
public class Login {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "u_id")
    private Integer uid;
    
    @Column(name = "status")
    private Boolean status;
    
    @Column(name = "fp_token")
    private String forgetpasswordtoken;
    
    @Column(name = "fp_token_at")
    private Date forgetpasswordtokenat;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @Column(name = "created_by")
    private Integer createdBy;
    
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @Column(name = "updated_by")
    private Integer updatedBy;
}