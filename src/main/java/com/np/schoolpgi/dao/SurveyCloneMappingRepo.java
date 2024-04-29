package com.np.schoolpgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.SurveyCloneMapping;

@Repository
public interface SurveyCloneMappingRepo extends JpaRepository<SurveyCloneMapping, Long>{

}
