package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.model.EventLog;

public interface EventLogService {
	
	List<EventLog> getEventLogs(Integer userId);
	void saveEventLogs(Integer eventId, String eventName,Integer userId,String request, String response);
}
