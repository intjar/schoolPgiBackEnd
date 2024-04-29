package com.np.schoolpgi.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.ExcelSignature;

@Repository
public interface ExcelSignatureRepository extends JpaRepository<ExcelSignature,Integer>{
	
	Optional<ExcelSignature> findBySurveyId(Integer id);

}
