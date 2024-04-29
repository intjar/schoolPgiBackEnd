package com.np.schoolpgi.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import lombok.Data;

@NamedStoredProcedureQuery(
		name = "get_remarks_data_by_id",
		procedureName = "get_remarks_data_by_id",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "sur_id", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "deo_id", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.INOUT, name = "_jsontext", type = String.class)
		})
@NamedStoredProcedureQuery(
		name = "get_survey_data_list_review",
		procedureName = "get_survey_data_list_review",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "usr_id", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "inp_pg", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.INOUT, name = "_jsontext", type = String.class)
		})

@Data
@Entity
@Table(name = "survey_data_entry_remarks")
public class SurveyDataEntryRemarks {
	
	@Id
	private Long id;
}
