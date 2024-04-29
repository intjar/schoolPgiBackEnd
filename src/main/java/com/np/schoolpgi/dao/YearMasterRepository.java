package com.np.schoolpgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.YearMaster;
import com.np.schoolpgi.model.YearMasterCompositeKey;

@Repository
public interface YearMasterRepository extends JpaRepository<YearMaster, YearMasterCompositeKey>{

}
