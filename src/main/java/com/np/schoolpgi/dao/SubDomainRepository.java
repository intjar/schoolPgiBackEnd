package com.np.schoolpgi.dao;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.SubDomain;

@Repository
public interface SubDomainRepository extends JpaRepository<SubDomain, Integer>{
	
	Optional<SubDomain> findBySubDomainCode(@NotNull Integer subDomainCode);

	Optional<SubDomain> findByDomainMasterIdAndSubDomainNameIgnoreCase(Integer id, String subDomainName);

	Optional<SubDomain> findByDomainMasterIdAndSubDomainNameIgnoreCaseAndIdNot(Integer id, String subDomainName, Integer id2);

	Optional<SubDomain> findBySubDomainCodeAndIdNot(@NotNull Integer subDomainCode, Integer id);

}
