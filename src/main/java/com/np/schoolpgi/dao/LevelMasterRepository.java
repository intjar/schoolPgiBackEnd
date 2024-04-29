package com.np.schoolpgi.dao;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.LevelMaster;

@Repository
public interface LevelMasterRepository extends JpaRepository<LevelMaster, Integer>{
	@Query(value = "CALL get_level_master_data(:inp_type)", nativeQuery = true)
	String getLevelMasterData(@Param("inp_type") String inp_type);
	
	Optional<LevelMaster> findByLevelCode(Integer levelCode);

	Optional<LevelMaster> findByLevelCodeAndIdNot(@NotNull Integer levelCode, Integer id);
	
	@Query(value = "SELECT u FROM LevelMaster u WHERE u.id = :id")
	LevelMaster findByLevelId(@Param("id") Integer id);
	
	List<LevelMaster> findByIdIn(List<Integer> id);

	Optional<LevelMaster> findByLevelNameIgnoreCaseAndIdNot(String levelName, Integer id);

	Optional<LevelMaster> findByLevelNameIgnoreCase(String levelName);
	
	@Query(value = "Select u from LevelMaster u where u.id in (Select i.level  from InstanceMaster i where i.status=true)")
	List<LevelMaster> findAllChildLevel();

}
