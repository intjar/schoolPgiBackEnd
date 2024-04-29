package com.np.schoolpgi.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.SurveyMasterRepository;
import com.np.schoolpgi.dao.SurveyUserMappingRepo;
import com.np.schoolpgi.dto.response.AssignedSurvey;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.SurveyMaster;
import com.np.schoolpgi.model.SurveyUserMapping;
import com.np.schoolpgi.service.SurveyUserMappingService;

@Service
public class SurveyUserMappingServiceImpl implements SurveyUserMappingService {
	
	Logger LOGGER = LogManager.getLogger(SurveyUserMappingServiceImpl.class);

	@Autowired
	private SurveyUserMappingRepo surveyUserMappingRepo;
	
	@Autowired
	private SurveyMasterRepository surveyMasterRepository;
	
	@Override
	public String surveyUserMapping(String req) {
		String surveyUserMapping = surveyUserMappingRepo.surveyUserMapping(req);
		return surveyUserMapping;
	}

	@Override
	public String getUpdSurveySummary(Integer pageNo,Integer pageSize, String sortDir, String sortBy, String searchKey, Long login_id, Long sur_id, String inp_type, String _jsontext) {
		try {
			
			return surveyUserMappingRepo.getUpdSurveySummary(pageNo, pageSize, sortDir, sortBy,searchKey, login_id, sur_id, inp_type, _jsontext);
		} catch (Exception e) {
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	public List<AssignedSurvey> assignedSurveyList(Long loggedinUserId) {
		// TODO Auto-generated method stub
		List<SurveyUserMapping> surveyUserMappings=surveyUserMappingRepo.findByUserId(loggedinUserId);
		if(surveyUserMappings!=null)
		{
			List<AssignedSurvey> assignedSurveys=new ArrayList<>();
			for(SurveyUserMapping s: surveyUserMappings)
			{
				AssignedSurvey assignedSurvey=new AssignedSurvey();
				assignedSurvey.setUserId(s.getUserId());
				assignedSurvey.setYearCode(s.getYearCode());
				if(s.getDeoSurveyIds()!=null)
				{
					List<Integer> deoIds= Arrays.stream(s.getDeoSurveyIds().split(","))
				            .map(Integer::parseInt)
				            .collect(Collectors.toList());
					List<SurveyMaster> deoSurveys=new ArrayList<>();
					for(Integer i:deoIds)
					{
						Optional<SurveyMaster> survey=surveyMasterRepository.findById(i);
						if(survey.isPresent())
						{
							SurveyMaster su=survey.get();
							deoSurveys.add(su);
						}
						
					}
					assignedSurvey.setDeoSurvey(deoSurveys);
					
				}
				if(s.getApproverSurveyIds()!=null)
				{
					List<Integer> appIds= Arrays.stream(s.getApproverSurveyIds().split(","))
				            .map(Integer::parseInt)
				            .collect(Collectors.toList());
					List<SurveyMaster> appSurveys=new ArrayList<>();
					for(Integer i:appIds)
					{
						Optional<SurveyMaster> survey=surveyMasterRepository.findById(i);
						if(survey.isPresent())
						{
							SurveyMaster su=survey.get();
							appSurveys.add(su);
						}
						
					}
					assignedSurvey.setApprovedSurvey(appSurveys);
					
				}
				if(s.getViewerSurveyIds()!=null)
				{
					List<Integer> viewerIds= Arrays.stream(s.getViewerSurveyIds().split(","))
				            .map(Integer::parseInt)
				            .collect(Collectors.toList());
					List<SurveyMaster> viewerSurveys=new ArrayList<>();
					for(Integer i:viewerIds)
					{
						Optional<SurveyMaster> survey=surveyMasterRepository.findById(i);
						if(survey.isPresent())
						{
							SurveyMaster su=survey.get();
							viewerSurveys.add(su);
						}
						
					}
					assignedSurvey.setViewerSurvey(viewerSurveys);
					
				}
				assignedSurveys.add(assignedSurvey);
			}
			return 	assignedSurveys;
		}
		
		return null;
		
	}
	
	

}
