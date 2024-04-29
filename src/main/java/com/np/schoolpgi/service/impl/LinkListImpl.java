package com.np.schoolpgi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.LinkListRepository;
import com.np.schoolpgi.model.LinkList;
import com.np.schoolpgi.service.LinkListService;

@Service
public class LinkListImpl implements LinkListService {

	@Autowired
	LinkListRepository linkListRepository;
	
	@Override
	public List<LinkList> getLinkList() {
		
		
		List<LinkList> linkListData=linkListRepository.findAll();
		
	return linkListData;
		
	}
	
	

}
