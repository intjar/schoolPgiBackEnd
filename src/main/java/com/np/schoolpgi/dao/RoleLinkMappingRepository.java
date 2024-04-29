package com.np.schoolpgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.RoleLinkMappings;


@Repository
public interface RoleLinkMappingRepository extends JpaRepository<RoleLinkMappings, Integer>{
	
	RoleLinkMappings findByRoleId(Integer roleId);
	
	

}
