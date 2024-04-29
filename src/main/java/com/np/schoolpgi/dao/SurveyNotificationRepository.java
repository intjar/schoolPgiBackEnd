package com.np.schoolpgi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.np.schoolpgi.model.SurveyNotification;

public interface SurveyNotificationRepository extends JpaRepository<SurveyNotification, Long> {

	@Query(value = "SELECT * FROM public.survey_notification sn LEFT JOIN survey_master sm ON sn.survey_id=sm.id WHERE sn.survey_id=:survey_id", nativeQuery = true)
	SurveyNotification findSurveyNotificationBySurveyId(@Param("survey_id") Integer survey_id);

	@Query(value = "SELECT survey_id FROM public.survey_notification WHERE status=:status", nativeQuery = true)
	List<Integer> findNotificationByStatus(@Param("status") String status);
	
	@Query(value = "SELECT status FROM public.survey_notification WHERE survey_id = CAST(?1 AS bigint)", nativeQuery = true)
	String getStatusBySurveyId(String surveyId);

}
