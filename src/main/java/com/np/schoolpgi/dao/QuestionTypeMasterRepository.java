package com.np.schoolpgi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.QuestionTypeMaster;

@Repository
public interface QuestionTypeMasterRepository extends JpaRepository<QuestionTypeMaster, Integer> {

	List<QuestionTypeMaster> findByAnswerTypeNot(String str);
}
