package com.np.schoolpgi.dao;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.SubQuestion;

@Repository
public interface SubQuestionRepository extends JpaRepository<SubQuestion, Long>{

	Optional<SubQuestion> findBySubQuestionCodeAndIdNot(Integer subQuestionCode, Long id);

	Optional<SubQuestion> findByQuestionMasterIdAndQuestionTypeMasterIdAndSubQuestionIgnoreCaseAndIdNot(Long id, Integer id2,
			String subQuestion, Long id3);

	Optional<SubQuestion> findByQuestionMasterIdAndQuestionTypeMasterIdAndSubQuestionIgnoreCase(Long id, Integer id2,
			String subQuestion);

	Optional<SubQuestion> findBySubQuestionCode(@NotNull Integer subQuestionCode);

}
