package com.np.schoolpgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.EventMaster;


@Repository
public interface EventMasterRepository extends JpaRepository<EventMaster, Integer> {

}
