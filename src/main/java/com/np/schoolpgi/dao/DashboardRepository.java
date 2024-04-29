package com.np.schoolpgi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.np.schoolpgi.model.SurveyMaster;

@Repository
public interface DashboardRepository extends JpaRepository<SurveyMaster, Integer> {

	@Query(value = "SELECT * FROM survey_master s LEFT JOIN role_master r on s.approver_role_id = r.id LEFT JOIN level_master l on s.approver_level_id = l.id WHERE s.status != 2 AND s.year_code=case when :yearCode ='' then s.year_code else :yearCode end;", nativeQuery = true)
	List<SurveyMaster> findAllSurvey(@Param("yearCode") String yearCode);

	@Query(value = "SELECT count(survey_id) FROM survey_map_question WHERE survey_id=:surveyId AND is_mandatory=0", nativeQuery = true)
	int isMapped(@Param("surveyId") String surveyId);

	@Query(value = "SELECT * FROM survey_master WHERE created_by=:id", nativeQuery = true)
	List<SurveyMaster> findByCreatedBy(@Param("id") int id);

	@Query(value = "SELECT unnest(string_to_array(approver_survey_ids, ',')) AS survey_id FROM survey_user_mapping  WHERE user_id=:id "
			+ "UNION "
			+ "SELECT unnest(string_to_array(reviewer_survey_ids, ',')) AS survey_id FROM survey_user_mapping  WHERE user_id=:id "
			+ "UNION "
			+ "SELECT unnest(string_to_array(viewer_survey_ids, ',')) AS survey_id FROM survey_user_mapping  WHERE user_id=:id "
			+ "UNION "
			+ "SELECT unnest(string_to_array(deo_survey_ids, ',')) AS survey_id FROM survey_user_mapping  WHERE user_id=:id ;", nativeQuery = true)
	List<Integer> findAllSurveyIdByLoginId(@Param("id") int id);

	@Query(value = "SELECT * FROM survey_master WHERE id in (:surveyIdList)", nativeQuery = true)
	List<SurveyMaster> findAllSurveyById(@Param("surveyIdList") List<Integer> surveyIdList);

	@Query(value = "SELECT deo_level_id FROM survey_master WHERE id= :id", nativeQuery = true)
	int findSurveyLevelId(@Param("id") int id);

	@Query(value = "SELECT level_id from user_details WHERE u_id= :u_id", nativeQuery = true)
	int findUserLevelId(@Param("u_id") int u_id);

	@Query(value = "SELECT id FROM level_master WHERE parent_level_id IN :parentLevelIds", nativeQuery = true)
	List<Integer> findUserChildLevelId(List<Integer> parentLevelIds);
	
	@Query(value = "SELECT id, level_name from level_master WHERE id IN :levelIds", nativeQuery = true)
	List<Object[]> findLevelNameByLevelId(List<Integer> levelIds);
	
	@Query(value = "Call refresh_website_tables_data(:year_code)", nativeQuery = true)
	String moveWebsiteTables(@PathVariable("year_code") Integer year_code);
}
