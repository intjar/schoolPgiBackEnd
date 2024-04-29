package com.np.schoolpgi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.EventLog;

@Repository
public interface EventLogsRepository extends JpaRepository<EventLog,Long>{
	
	@Query("SELECT u FROM EventLog u WHERE u.userId = :id")
	List<EventLog> findEventLogsByUserId(@Param("id") Integer id);

}
