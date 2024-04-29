package com.np.schoolpgi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.NotificationData;

@Repository
public interface NotificationDataRepository extends JpaRepository<NotificationData, Integer>{
	
	List<NotificationData> findByToWhomUserId(Integer uid);
}
