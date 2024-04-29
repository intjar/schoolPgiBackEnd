package com.np.schoolpgi.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.SurveyMapQuestion;

@Repository
public interface SurveyMapQuestionRepo extends JpaRepository<SurveyMapQuestion, Integer> {

	Optional<SurveyMapQuestion> findBySurveyMasterIdAndDomainMasterIdAndSubDomainIdAndQuestionMasterId(Integer surveyId,
			Integer domainId, Integer subDomainId, Long questId);

	@Query(value = "SELECT smq.* FROM public.survey_map_question smq LEFT JOIN public.survey_master sm ON smq.survey_id=sm.id \r\n"
			+ "LEFT JOIN public.domain_master dm ON smq.domain_id=dm.id\r\n"
			+ "LEFT JOIN public.sub_domain sdm ON smq.sub_domain_id=sdm.id\r\n"
			+ "LEFT JOIN public.question_master qm ON smq.question_id=qm.id\r\n"
			+ "LEFT JOIN public.data_source ds ON smq.data_source_id=ds.id WHERE smq.survey_id=:survey_id AND \r\n"
			+ "(LOWER(dm.domain_name) LIKE LOWER(CONCAT('%',:searchKey,'%')) OR \r\n"
			+ "LOWER(sdm.sub_domain_name) LIKE LOWER(CONCAT('%',:searchKey,'%')) OR \r\n"
			+ "LOWER(qm.question) LIKE LOWER(CONCAT('%',:searchKey,'%')))", nativeQuery = true)
	Page<SurveyMapQuestion> findSurveyMapQuestBySurveyMasterId(@Param("survey_id") Integer survey_id,
			@Param("searchKey") String searchKey, Pageable pageable);

	@Query(value = "SELECT * FROM survey_map_question u WHERE u.survey_id = ?1 and u.is_third_party = 1", nativeQuery = true)
	List<SurveyMapQuestion> findBySurveyMasterIdAndIsthird(Integer id);

//	Optional<SurveyMapQuestion> findBySurveyMasterIdAndDomainMasterIdAndSubDomainIdAndQuestionMasterIdAndIdNot(
//			Integer surveyId, 
//			Integer domainId, 
//			Integer subDOmainId, 
//			Long questId, 
//			Integer id);

	List<SurveyMapQuestion> findByDomainMasterId(Integer id);

	List<SurveyMapQuestion> findBySubDomainId(Integer id);

	List<SurveyMapQuestion> findByQuestionMasterId(Long id);

	Optional<SurveyMapQuestion> findBySurveyMasterIdAndQuestionMasterId(Integer id, Long id2);

	Optional<SurveyMapQuestion> findBySurveyMasterIdAndQuestionMasterIdAndIdNot(Integer id, Long id2, Integer id3);

	List<SurveyMapQuestion> findBySurveyMasterId(Integer id);
	
	@Query(value = "SELECT smq.* FROM question_master qm INNER JOIN survey_map_question smq ON qm.id = smq.question_id WHERE smq.survey_id = ?1 AND qm.status = 't';", nativeQuery = true)
	List<SurveyMapQuestion> findActiveQuesBySurveyMasterId(Integer surveyId);

	@Query(value = "SELECT MAX(order_id) FROM survey_map_question u where u.survey_id = ?1", nativeQuery = true)
	Integer findMaxOrderId(Integer surveyId);

//	Optional<SurveyMapQuestion> findBySurveyMasterId(@NotNull Integer surveyId);

}
