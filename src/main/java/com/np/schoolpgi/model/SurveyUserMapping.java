package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import lombok.Data;

@NamedStoredProcedureQuery(
		name = "assign_survey_user_mapping", 
		procedureName = "assign_survey_user_mapping", 
		parameters = {
		@StoredProcedureParameter(mode = ParameterMode.INOUT, name = "_jsontext",type = String.class)}
)

@NamedStoredProcedureQuery(
		name = "get_upd_survey_summary",
		procedureName = "get_upd_survey_summary",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "login_id", type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "sur_id", type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.INOUT, name = "inp_type", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.INOUT, name = "_jsontext", type = String.class)
		})

@Table(name = "survey_user_mapping")
@Data
@Entity
public class SurveyUserMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Long userId;
	private String yearCode;
	private Integer levelId;
	private Integer instanceId;
	private String deoSurveyIds;
	private String approverSurveyIds;
	private String reviewerSurveyIds;
	private String viewerSurveyIds;
	private Date createdAt;
	private Long createdBy;
	private Date updatedAt;
	private Long updatedBy;
}
