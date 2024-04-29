package com.np.schoolpgi.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.np.schoolpgi.model.SurveyMaster;

@Repository
public interface SurveyMasterRepository extends JpaRepository<SurveyMaster, Integer> {

	Optional<SurveyMaster> findByYearCodeAndSurveyNameIgnoreCase(String yearCode, String surveyName);

	@Query(value = "SELECT u FROM SurveyMaster u WHERE u.yearCode = :yearcode and u.surveyName = :surveyname and u.id <> :id")
	Optional<SurveyMaster> findbysurvayname(@Param("yearcode") String yearCode, @Param("surveyname") String surveyName,
			@Param("id") Integer id);

	// Optional<SurveyMaster> findById(Integer id);

	@Query(value = "Call get_survey_data_entry_by_id(:sur_id,:is_third,:lg_id,:inst_id,:_jsontext)", nativeQuery = true)
	String surveyDataEntryById(@PathVariable("sur_id") Integer sur_id, @PathVariable("is_third") Integer is_third,
			@PathVariable("lg_id") Integer lg_id, @PathVariable("inst_id") Integer inst_id,
			@PathVariable("_jsontext") String _jsontext);

	@Query(value = "Call get_survey_data_list(:page_no,:page_size,:sort_type,:sort_col,:search_text,:is_third,:lg_id,:year_code,:_jsontext)", nativeQuery = true)
	String getSurveyListForDeo(@PathVariable("page_no") Integer page_no, @PathVariable("page_size") Integer page_size,
			@PathVariable("sort_type") String sort_type, @PathVariable("sort_col") String sort_col,
			@PathVariable("search_text") String search_text, @PathVariable("is_third") Integer is_third,
			@PathVariable("lg_id") Integer lg_id, @PathVariable("year_code") String year_code,
			@PathVariable("_jsontext") String _jsontext);

	@Query(value = "Call insert_survey_data_entry(:_jsontext)", nativeQuery = true)
	String insertSurveyDataEntry(@PathVariable("_jsontext") String _jsontext);

	@Query(value = "SELECT * FROM survey_master s LEFT JOIN role_master r on s.approver_role_id = r.id LEFT JOIN level_master l on s.approver_level_id = l.id "
			+ "WHERE " + "(LOWER(s.survey_name) LIKE LOWER(concat('%',:searchKey,'%')) OR "
			+ "LOWER(s.survey_description) LIKE LOWER(concat('%',:searchKey,'%')) OR "
			+ "(CONCAT(s.year_code,'')) LIKE (concat('%',:searchKey,'%')) OR "
			+ "(CONCAT(s.survey_start_date,'')) LIKE (concat('%',:searchKey,'%')) OR "
			+ "(CONCAT(s.survey_end_date,'')) LIKE (concat('%',:searchKey,'%'))) AND s.status != 2 AND "
			+ "s.year_code=case when :yearCode ='' then s.year_code else :yearCode end", nativeQuery = true)
	Page<SurveyMaster> findAllSurvey(Pageable pageable, @Param("searchKey") String searchKey,
			@Param("yearCode") String yearCode);

	@Query(value = "SELECT * FROM survey_master s LEFT JOIN role_master r on s.approver_role_id = r.id LEFT JOIN level_master l on s.approver_level_id = l.id "
			+ "WHERE s.id= :id AND s.status != 2", nativeQuery = true)
	Optional<SurveyMaster> findDetailsById(@Param("id") int id);

//	@Query(value = "SELECT * from survey_master WHERE survey_end_date > now() and review_mandatory = 1", nativeQuery = true)
//	List<SurveyMaster> findAllSurveyIdReviewMandat();

	@Query(value = "SELECT * FROM survey_master WHERE created_by= :id AND survey_end_date > now() AND status = 1 AND notify_status IN ('T','D')", nativeQuery = true)
	List<SurveyMaster> findAllActiveSurvey(@Param("id") int id);

	@Query(value = "SELECT procedure_name FROM survey_master WHERE id= :id", nativeQuery = true)
	String findProcedureBySurveyId(@Param("id") int id);

	@Query(value = "Call get_survey_status_by_login(:page_no,:page_size,:sort_type,:sort_col,:search_text,:lg_id,:filter_status,:_jsontext)", nativeQuery = true)
	String getSurveyStatusByLogin(@PathVariable("page_no") Integer page_no,
			@PathVariable("page_size") Integer page_size, @PathVariable("sort_type") String sort_type,
			@PathVariable("sort_col") String sort_col, @PathVariable("search_text") String search_text,
			@PathVariable("lg_id") Integer lg_id, @PathVariable("filter_status") String filter_status,
			@PathVariable("_jsontext") String _jsontext);

	@Query(value = "call get_survey_status_by_login_details(:page_no,:page_size,:sort_type,:sort_col,:search_text,:lg_id,:survey_id,:_jsontext)", nativeQuery = true)
	String getInstanceWiseList(@PathVariable("page_no") Integer page_no, @PathVariable("page_size") Integer page_size,
			@PathVariable("sort_type") String sort_type, @PathVariable("sort_col") String sort_col,
			@PathVariable("search_text") String search_text, @PathVariable("lg_id") Integer lg_id,
			@PathVariable("survey_id") Integer survey_id, @PathVariable("_jsontext") String _jsontext);

	@Query(value = "call get_survey_data_rptwise(:page_no,:page_size,:sort_type,:sort_col,:search_text,:lg_id,:survey_id,:rpt_lvl,:_jsontext)", nativeQuery = true)
	String getSurveyDataAccToReport(@PathVariable("page_no") Integer page_no,
			@PathVariable("page_size") Integer page_size, @PathVariable("sort_type") String sort_type,
			@PathVariable("sort_col") String sort_col, @PathVariable("search_text") String search_text,
			@PathVariable("lg_id") Integer lg_id, @PathVariable("survey_id") Integer survey_id,
			@PathVariable("rpt_lvl") Integer rpt_lvl, @PathVariable("_jsontext") String _jsontext);

}
