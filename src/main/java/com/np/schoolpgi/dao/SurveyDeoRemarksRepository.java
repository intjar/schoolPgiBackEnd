package com.np.schoolpgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.np.schoolpgi.model.SurveyDataEntryRemarks;

@Repository
public interface SurveyDeoRemarksRepository extends JpaRepository<SurveyDataEntryRemarks, Long>{

	@Query(value="Call get_remarks_data_by_id(:sur_id,:deo_id,:_jsontext)", nativeQuery = true)
	String getRemarks(
			@PathVariable("sur_id") Integer sur_id, 
			@PathVariable("deo_id") Integer deo_id, 
			@PathVariable("_jsontext") String _jsontext);
	
	@Query(value="Call get_survey_data_list_review(:page_no,:page_size, :sort_type, :sort_col, :search_text, :usr_id,:inp_pg,:year_code,:_jsontext)", nativeQuery = true)
	String getSurveyDataListReview(
			@PathVariable("page_no") Integer page_no,
			@PathVariable("page_size") Integer page_size,
			@PathVariable("sort_type") String sort_type,
			@PathVariable("sort_col") String sort_col,
			@PathVariable("search_text") String search_text,
			@PathVariable("usr_id") Integer usr_id, 
			@PathVariable("inp_pg") String inp_pg, 
			@PathVariable("year_code") String year_code, 
			@PathVariable("_jsontext") String _jsontext);
	
	@Query(value="Call get_survey_data_list_review_details(:sur_id,:deo_id,:_jsontext)", nativeQuery = true)
	String surveyDataListReviewDetails(
			@PathVariable("sur_id") Integer sur_id, 
			@PathVariable("deo_id") Integer deo_id, 
			@PathVariable("_jsontext") String _jsontext);
	
	@Query(value="Call upd_survey_review_approve(:_jsontext)", nativeQuery = true)
	String updSurveyReviewApprove(
			@PathVariable("_jsontext") String _jsontext);
	
}
