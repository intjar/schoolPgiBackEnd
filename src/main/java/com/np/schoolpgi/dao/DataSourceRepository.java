package com.np.schoolpgi.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.DataSource;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Integer>{

	Optional<DataSource> findByNameIgnoreCase(String name);

	Optional<DataSource> findByCode(Integer code);
	
	//Used in UPDATE API to check Duplicate DATA SOURCE NAME
	Optional<DataSource> findByNameIgnoreCaseAndIdNot(String name, Integer id);
	
	//Used in UPDATE API to check Duplicate DATA SOURCE CODE
	Optional<DataSource> findByCodeAndIdNot(Integer code, Integer id);

}
