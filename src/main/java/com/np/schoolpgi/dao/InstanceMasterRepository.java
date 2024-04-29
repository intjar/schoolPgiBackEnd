package com.np.schoolpgi.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.InstanceMaster;


@Repository
public interface InstanceMasterRepository extends JpaRepository<InstanceMaster, Integer>{
	
	@Query("SELECT u FROM InstanceMaster u WHERE u.level = :id")
	List<InstanceMaster> findInstanceByBlockLevelId(@Param("id") Integer id);
	 Optional<InstanceMaster> findByInstanceCode(Long instanceCode);
	 List<InstanceMaster> findByInstanceNameIgnoreCase(String instanceName);
      
		@Query(value = "SELECT u FROM InstanceMaster u WHERE u.instanceCode = :instancecode and u.id <> :id")
		Optional<InstanceMaster> findByInstanceCodeUpdate(@Param("instancecode") Long code,@Param("id") Integer id);
		
		@Query(value = "SELECT u FROM InstanceMaster u WHERE u.id <> :id and u.instanceName = :instancename")
		List<InstanceMaster> findByInstanceNameUpdate(@Param("id") Integer id,@Param("instancename") String instanceName);
		
		List<InstanceMaster> findByLevelIn(List<Integer> viewByListIds);
		
		@Query(value =  "SELECT ch.*\r\n"
				
				+ "\r\n"
				+ ",pt.id parent_intance_id ,pt.Instance_name parent_instance_name\r\n"
				+ "\r\n"
				+ ",lv.level_id,lv.level_name, lv.parent_level_id,\r\n"
				+ "\r\n"
				+ "lv.parent_level_name,ch.instance_code code,ch.status\r\n"
				+ "\r\n"
				+ "FROM\r\n"
				+ "\r\n"
				+ "instance_master ch\r\n"
				+ "LEFT JOIN instance_master pt\r\n"
				+ "ON (ch.parent_instance_id = pt.id)\r\n"
				+ "LEFT JOIN\r\n"
				+ "(\r\n"
				+ "SELECT lm.id level_id,lm.level_name,plm.id parent_level_id,plm.level_name parent_level_name\r\n"
				+ "FROM level_master lm \r\n"
				+ "LEFT JOIN\r\n"
				+ "level_master plm\r\n"
				+ "ON (lm.parent_level_id = plm.id)\r\n"
				+ "\r\n"
				+ ") lv\r\n"
				+ "\r\n"
				+ "ON (ch.level_id = lv.level_id)\r\n"
				+ "\r\n"
				+ "WHERE ch.id NOT IN (Select instance_id from public.school_master) AND (LOWER(ch.Instance_name) like  lower(concat('%',:searchKey,'%'))\r\n"
				+ "\r\n"
				+ "OR LOWER(pt.Instance_name) like  lower(concat('%',:searchKey,'%'))\r\n"
				+ "\r\n"
				+ "OR LOWER(lv.level_name) like  lower(concat('%',:searchKey,'%'))\r\n"
				+ "\r\n"
				+ "OR LOWER(lv.parent_level_name) like  lower(concat('%',:searchKey,'%')) OR (CONCAT(ch.instance_code,'' )) like (concat('%',:searchKey,'%')))",nativeQuery = true)
		Page<InstanceMaster> findInstanceWithoutSchool(@Param("searchKey") String searchKey,
				Pageable pageable);
//		List<InstanceMaster> findInstanceWithoutSchool();
		
		List<InstanceMaster> findByLevel(Integer id);
		
		@Query(value = "SELECT ch.*\r\n"
				
				+ "\r\n"
				+ ",pt.id parent_intance_id ,pt.Instance_name parent_instance_name\r\n"
				+ "\r\n"
				+ ",lv.level_id,lv.level_name, lv.parent_level_id,\r\n"
				+ "\r\n"
				+ "lv.parent_level_name,ch.instance_code code,ch.status\r\n"
				+ "\r\n"
				+ "FROM\r\n"
				+ "\r\n"
				+ "instance_master ch\r\n"
				+ "LEFT JOIN instance_master pt\r\n"
				+ "ON (ch.parent_instance_id = pt.id)\r\n"
				+ "LEFT JOIN\r\n"
				+ "(\r\n"
				+ "SELECT lm.id level_id,lm.level_name,plm.id parent_level_id,plm.level_name parent_level_name\r\n"
				+ "FROM level_master lm \r\n"
				+ "LEFT JOIN\r\n"
				+ "level_master plm\r\n"
				+ "ON (lm.parent_level_id = plm.id)\r\n"
				+ "\r\n"
				+ ") lv\r\n"
				+ "\r\n"
				+ "ON (ch.level_id = lv.level_id)\r\n"
				+ "\r\n"
				+ "WHERE LOWER(ch.Instance_name) like  lower(concat('%',:searchKey,'%'))\r\n"
				+ "\r\n"
				+ "OR LOWER(pt.Instance_name) like  lower(concat('%',:searchKey,'%'))\r\n"
				+ "\r\n"
				+ "OR LOWER(lv.level_name) like  lower(concat('%',:searchKey,'%'))\r\n"
				+ "\r\n"
				+ "OR LOWER(lv.parent_level_name) like  lower(concat('%',:searchKey,'%')) OR (CONCAT(ch.instance_code,'')) like (concat('%',:searchKey,'%'))", nativeQuery = true)

		Page<InstanceMaster> findAllInstance( @Param("searchKey") String searchKey,
				Pageable pageable);
		
		

		@Query(value = "SELECT * FROM instance_master WHERE status=true", nativeQuery = true)
		List<InstanceMaster> findChildInstances();
}
