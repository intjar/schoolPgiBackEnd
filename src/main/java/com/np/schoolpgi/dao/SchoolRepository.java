package com.np.schoolpgi.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.SchoolMaster;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolMaster, Long>{

	Optional<SchoolMaster> findByUdiseCode(String udiseCode);

	Optional<SchoolMaster> findByUdiseCodeAndIdNot(String udiseCode, Long id);

	SchoolMaster findBySchoolNameIgnoreCase(String schoolName);

	SchoolMaster findBySchoolNameIgnoreCaseAndIdNot(String schoolName, Long id);

	@Query(value = "SELECT sch.*,inst.parent_instance_id, inst.instance_code FROM public.school_master sch\r\n"
			+ "INNER JOIN \r\n"
			+ "public.instance_master inst on sch.block_level_instance_id=inst.id\r\n"
			+ "WHERE \r\n"
			+ "LOWER(sch.school_name) LIKE  LOWER(concat('%',:searchKey,'%')) OR\r\n"
			+ "LOWER(inst.instance_name) LIKE  LOWER(concat('%',:searchKey,'%')) OR\r\n"
			+ "CONCAT(inst.instance_code, '') LIKE (concat('%',:searchKey,'%')) OR\r\n"
			+ "CONCAT(sch.udise_code,'') LIKE (concat('%',:searchKey,'%'))", nativeQuery = true)
	Page<SchoolMaster> findAllSchools(Pageable pageable,@Param("searchKey") String searchKey);

}
