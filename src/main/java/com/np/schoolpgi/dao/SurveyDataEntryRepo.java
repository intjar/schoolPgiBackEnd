package com.np.schoolpgi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyDataEntryRepo extends JpaRepository<SurveyDataEntry, Long> {

	@Query(value = "SELECT * FROM survey_data_entry WHERE created_by = :userid", nativeQuery = true)
	List<SurveyDataEntry> getDataEntryDetails(@Param("userid") int userId);

	@Query(value = "SELECT survey_id, created_by FROM survey_data_entry WHERE created_by IN (:userid)", nativeQuery = true)
	List<int[]> getDataEntryDetailsByUserIds(@Param("userid") List<Integer> userId);

	@Query(value = "SELECT survey_id, status FROM survey_data_entry WHERE survey_id= CAST(:surveyId AS INTEGER) AND status='A'", nativeQuery = true)
	List<Object[]> getDataEntryDetailsByServeyIds(@Param("surveyId") String surveyId);

	@Query(value = "SELECT * FROM survey_data_entry WHERE survey_id= CAST(:surveyId AS INTEGER)", nativeQuery = true)
	List<SurveyDataEntry> getDataEntryByServeyIds(@Param("surveyId") String surveyId);
	
	@Query(value = "SELECT * FROM survey_data_entry WHERE survey_id= CAST(:surveyId AS INTEGER) AND created_by= :userId", nativeQuery = true)
	List<SurveyDataEntry> getDataEntryByServeyIdAndUserId(@Param("surveyId") String surveyId, @Param("userId") Integer userId);
}
