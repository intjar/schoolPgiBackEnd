package com.np.schoolpgi.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.EventLogsRepository;
import com.np.schoolpgi.dao.EventMasterRepository;
import com.np.schoolpgi.model.EventLog;
import com.np.schoolpgi.service.EventLogService;

@Service
public class EventLogServiuceImpl implements EventLogService {

	@Autowired
	EventLogsRepository eventLogsRepository;
	
	@Autowired
	EventMasterRepository eventMasterRepository;

	@Override
	public List<EventLog> getEventLogs(Integer userId) {
		// TODO Auto-generated method stub
//		
//		if(userId==1)
//		{
//			List<EventLog> eventLogs=eventLogsRepository.findAll();
//			return eventLogs;
//		}
//		else
		{
			List<EventLog> eventLogs = eventLogsRepository.findEventLogsByUserId(userId);
			return eventLogs;
		}

	}

	@Override
	public void saveEventLogs(Integer eventId, String eventName, Integer userId, String request, String response) {
		// TODO Auto-generated method stub

		EventLog eventLog = new EventLog();

		eventLog.setRequest(request);
		eventLog.setResponse(response);
		eventLog.setCreatedAt(new Date());
		eventLog.setEventId(eventId);
		eventLog.setEventName(eventName);
		eventLog.setUserId(userId);
		eventLogsRepository.save(eventLog);
	
	}

}
