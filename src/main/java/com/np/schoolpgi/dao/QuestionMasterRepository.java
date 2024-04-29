package com.np.schoolpgi.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.QuestionMaster;

@Repository
public interface QuestionMasterRepository extends JpaRepository<QuestionMaster, Long>{

	Optional<QuestionMaster> findByQuestionTypeMasterIdAndQuestionIgnoreCaseAndIdNot(Integer questionTypeId, String question, Long id);

	Optional<QuestionMaster> findByQuestionCode(Integer questionCode);

	Optional<QuestionMaster> findByQuestionCodeAndIdNot(Integer questionCode, Long id);

	Optional<QuestionMaster> findByQuestionTypeMasterIdAndQuestionIgnoreCase(Integer id, String question);

	@Query(value = "SELECT * FROM question_master \r\n"
			+ "WHERE id not in\r\n"
			+ "(SELECT DISTINCT question_id FROM survey_map_question\r\n"
			+ "WHERE survey_id = :survey_id)", nativeQuery = true)
	List<QuestionMaster> findSurveyMapQuests(
			@Param("survey_id") Integer survey_id);

}
