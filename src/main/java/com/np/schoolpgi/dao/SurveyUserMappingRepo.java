package com.np.schoolpgi.dao;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.np.schoolpgi.model.SurveyUserMapping;

@Repository
public interface SurveyUserMappingRepo extends JpaRepository<SurveyUserMapping, Integer> {

	@Query(value = "CALL assign_survey_user_mapping(:_jsontext)", nativeQuery = true)
	String surveyUserMapping(@PathVariable(name = "_jsontext") String _jsontext);
	
	
	//View And Update Assign Survey User Mapping
	@Query(value = "CALL get_upd_survey_summary(:page_no,:page_size,:sort_type,:sort_col,:search_text,:login_id,:sur_id,:inp_type,:_jsontext)", nativeQuery = true)
	String getUpdSurveySummary(
			@PathVariable("page_no") Integer page_no,
			@PathVariable("page_size") Integer page_size,
			@PathVariable("sort_type") String sort_type,
			@PathVariable("sort_col") String sort_col,			
			@PathVariable("search_text") String search_text,
			@PathVariable(name="login_id") Long login_id,
			@PathVariable(name = "sur_id") Long sur_id,
			@PathVariable(name = "inp_type") String inp_type,
			@PathVariable(name = "_jsontext") String _jsontext
			);
	List<SurveyUserMapping> findByUserId(@NotNull Long userId);
	
	
	@Query(value = "Call survey_auto_assign(:inp_sur_id,:_jsontext)", nativeQuery = true)
	String surveyAutoAssign(
			@PathVariable("inp_sur_id") Integer inp_sur_id,
			@PathVariable("_jsontext") String _jsontext);
	
	@Query(value = "Call survey_assign_unassign(:_jsontext)", nativeQuery = true)
	String surveyAssignUnAssign(
			@PathVariable("_jsontext") String _jsontext);
	
	@Query(value = "CALL survey_notification (:inp_lg_id,:inp_sur_id,:nt_type,:_jsontext)", nativeQuery = true)
	String notifySurvey(
			@PathVariable(name="lg_id") Long inp_lg_id,
			@PathVariable(name = "s_id") Long inp_sur_id,
			@PathVariable(name = "nt_type") String nt_type,
			@PathVariable(name = "_jsontext") String _jsontext
			);
	
	@Query(value = "CALL rpt_admin_survey_assigned (:page_no,:page_size,:sort_type,:sort_col,:search_text,:lg_id,:lev_id,:inst_id,:_jsontext)", nativeQuery = true)
	String assignSurveyReport(
			@PathVariable("page_no") Integer page_no,
			@PathVariable("page_size") Integer page_size,
			@PathVariable("sort_type") String sort_type,
			@PathVariable("sort_col") String sort_col,			
			@PathVariable("search_text") String search_text,
			@PathVariable("lg_id") Integer lg_id,
			@PathVariable("lev_id") String lev_id,			
			@PathVariable("inst_id") String inst_id,
			@PathVariable(name = "_jsontext") String _jsontext
			);
	
	@Query(value = "CALL rpt_survey_assigned_instance_wise (:page_no,:page_size,:sort_type,:sort_col,:search_text,:sur_id ,:lg_id,:lev_id,:inst_id,:_jsontext)", nativeQuery = true)
	String surveyNameReport(
			@PathVariable("page_no") Integer page_no,
			@PathVariable("page_size") Integer page_size,
			@PathVariable("sort_type") String sort_type,
			@PathVariable("sort_col") String sort_col,			
			@PathVariable("search_text") String search_text,
			@PathVariable("sur_id ") Integer sur_id ,
			@PathVariable("lg_id") Integer lg_id,
			@PathVariable("lev_id") String lev_id,			
			@PathVariable("inst_id") String inst_id,
			@PathVariable(name = "_jsontext") String _jsontext
			);
	
	@Query(value = "CALL survey_tpdnotify_notify(:inp_survey_id,:_jsontext)", nativeQuery = true)
	String surveyTPDNotifyNotify(		
			@PathVariable("inp_survey_id") Integer inp_survey_id,
			@PathVariable("_jsontext") String _jsontext
			);
	
	@Query(value = "SELECT s.user_id, s.instance_id " +
	        "FROM survey_user_mapping s " +
	        "CROSS JOIN unnest(string_to_array(CAST(s.deo_survey_ids AS TEXT), ',')) AS ids(id) " +
	        "WHERE ids.id = ?1", nativeQuery = true)
	List<int[]> getIdBySurveyIds(String inp_survey_ids);
	
	
	@Query(value = "SELECT ?1, s.user_id " +
	        "FROM survey_user_mapping s " +
	        "CROSS JOIN unnest(string_to_array(CAST(s.deo_survey_ids AS TEXT), ',')) AS ids(id) " +
	        "WHERE ids.id = ?1", nativeQuery = true)
	List<int[]> getUserServeyMap(String inp_survey_ids);



}
