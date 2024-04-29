package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.request.NotificationFlagChangeRequest;
import com.np.schoolpgi.model.NotificationData;

public interface NotificationDataService {

	Integer createNotification(Integer reciever,Integer sender,Integer linkId,String description);
	List<NotificationData> getNotifications(Integer uid);
	Integer updateNotificationToReadUnread(NotificationFlagChangeRequest flagChangeRequest);
//	Integer updateNotificationToUnRead(Integer nId,Integer uid);
}
