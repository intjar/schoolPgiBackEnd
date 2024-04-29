package com.np.schoolpgi.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import lombok.Data;
//getSurveyDataEntryById
@NamedStoredProcedureQuery(
		name = "get_survey_data_entry_by_id",
		procedureName = "get_survey_data_entry_by_id",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "sur_id", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "is_third", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "lg_id", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.INOUT, name = "_jsontext", type = String.class)
		})
@NamedStoredProcedureQuery(
		name = "get_survey_data_list",
		procedureName = "get_survey_data_list",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "is_third", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "lg_id", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.INOUT, name = "_jsontext", type = String.class)
		})
@NamedStoredProcedureQuery(
		name = "insert_survey_data_entry",
		procedureName = "insert_survey_data_entry",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.INOUT, name = "_jsontext", type = String.class)
		})
@Entity
@Data
@Table(name = "survey_master")
public class SurveyMaster {
    
	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
  
    @Column(name="year_code", nullable = false)
    private String yearCode;
    
    @Column(name="survey_name", nullable = false)
    private String surveyName;
    
    
    @Column(name="survey_description")
    private String surveyDescription;
    
    @Column(name = "survey_start_date")
    private Date surveyStartDate;
    
    @Column(name = "survey_end_date")
    private Date surveyEndDate;
    
    @JoinColumn(name = "approver_level_id", referencedColumnName = "id")
    @ManyToOne
    private LevelMaster approverLevelId;
    
    @JoinColumn(name = "approver_role_id", referencedColumnName = "id")
    @ManyToOne
    private Roles approverRoleId;
    
    @Column(name="reviewer_level_id")
    private String reviewerLevelId;
    
    @Column(name = "reviewer_role_id")
    private String reviewerRoleId;
    
    @Column(name="viewer_level_id")
    private String viewerLevelId;
    
    @Column(name = "viewer_role_id")
    private String viewerRoleId;
    
    @JoinColumn(name = "deo_level_id", referencedColumnName = "id")
    @ManyToOne
    private LevelMaster deoLevelId;
    
    @JoinColumn(name = "deo_role_id", referencedColumnName = "id")
    @ManyToOne
    private Roles deoRoleId;
    
    
    @Column(name = "review_mandatory")
    private Integer reviewMandatory;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @Column(name = "created_by")
    private Integer createdBy;
     
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @Column(name = "updated_by")
    private Integer updatedBy;
    
    @Column(name="status")
    private Integer status;
    
    @Column(name="assigned_survey_status")
    private Boolean assignedSurveyStatus;
    
    @Column(name="procedure_name")
    private String procedureName;
    
}