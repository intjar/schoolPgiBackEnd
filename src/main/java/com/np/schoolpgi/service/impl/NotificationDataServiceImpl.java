package com.np.schoolpgi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.LinkListRepository;
import com.np.schoolpgi.dao.NotificationDataRepository;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.dto.request.NotificationFlagChangeRequest;
import com.np.schoolpgi.model.LinkList;
import com.np.schoolpgi.model.NotificationData;
import com.np.schoolpgi.model.User;
import com.np.schoolpgi.service.NotificationDataService;

@Service
public class NotificationDataServiceImpl implements NotificationDataService{

	@Autowired
	NotificationDataRepository notificationDataRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	LinkListRepository linkListRepository;
	
	@Override
	public List<NotificationData> getNotifications(Integer uid) {
		List<NotificationData> notificationDatas=notificationDataRepository.findByToWhomUserId(uid);
		return notificationDatas;
	}

	@Override
	public Integer createNotification(Integer reciever, Integer sender, Integer linkId, String description)
	{
		// TODO Auto-generated method stub
		
		NotificationData notificationData=new NotificationData();
		Optional<User> senderUser=userRepository.findById(sender);
		
		if(senderUser.isPresent())
		{
			notificationData.setFrom(senderUser.get());
		}
		else
		{
			return 2;
		}
		
		Optional<User> recieverUser=userRepository.findById(reciever);
		
		if(recieverUser.isPresent())
		{
			notificationData.setToWhom(recieverUser.get());
		}
		else
		{
			return 3;
		}

		Optional<LinkList> linkList=linkListRepository.findById(linkId);
		if(linkList.isPresent())
		{
			notificationData.setLinkId(linkList.get());
		}
		else
		{
			return 4;
		}
		notificationData.setDescription(description);
		
		notificationData.setCreatedBy(sender);
		
		notificationData.setCreatedAt(new Date());
		
		notificationData.setStatus(true);
		
		notificationData.setFlag(true);
		
      NotificationData save=  notificationDataRepository.save(notificationData);
        if(save==null)
		{
			return 0;
		}
		else
		{
			return 1;
		}
		
	}

	@Override
	public Integer updateNotificationToReadUnread(NotificationFlagChangeRequest flagChangeRequest) 
	{
		// TODO Auto-generated method stub
		Optional<NotificationData> notificationData=notificationDataRepository.findById(flagChangeRequest.getId());
		if(notificationData.isPresent())
		{
			NotificationData notificationData2=notificationData.get();
			notificationData2.setUpdatedAt(new Date());
			notificationData2.setUpdatedBy(flagChangeRequest.getLoggedInUserId());
			notificationData2.setFlag(flagChangeRequest.isFlag());
			notificationDataRepository.save(notificationData2);
			
		return 1;
		}
		return 0;
	}

//	@Override
//	public Integer updateNotificationToUnRead(Integer nId, Integer uid) {
//		
//				Optional<NotificationData> notificationData=notificationDataRepository.findById(nId);
//				if(notificationData.isPresent())
//				{
//					NotificationData notificationData2=notificationData.get();
//					notificationData2.setUpdatedAt(new Date());
//					notificationData2.setUpdatedBy(uid);
//					notificationData2.setFlag(true);
//				return 1;
//				}
//				return 0;
//	}
//	

}
