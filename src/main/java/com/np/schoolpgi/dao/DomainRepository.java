package com.np.schoolpgi.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.DomainMaster;

@Repository
public interface DomainRepository extends JpaRepository<DomainMaster, Integer>{

	Optional<DomainMaster> findByDomainNameIgnoreCase(String domainName);

	Optional<DomainMaster> findByDomainCode(Integer domainCode);

	Optional<DomainMaster> findByDomainNameIgnoreCaseAndIdNot(String domainName, Integer id);

	Optional<DomainMaster> findByDomainCodeAndIdNot(Integer domainCode, Integer id);

}
