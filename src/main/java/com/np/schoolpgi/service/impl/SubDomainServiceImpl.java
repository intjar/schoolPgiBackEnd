package com.np.schoolpgi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.DomainRepository;
import com.np.schoolpgi.dao.SubDomainRepository;
import com.np.schoolpgi.dao.SurveyMapQuestionRepo;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.SubDomainRequest;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.DomainMaster;
import com.np.schoolpgi.model.SubDomain;
import com.np.schoolpgi.model.SurveyMapQuestion;
import com.np.schoolpgi.service.SubDomainService;
import com.np.schoolpgi.util.StringUtils;

@Service
public class SubDomainServiceImpl implements SubDomainService {

	final static Logger LOGGER = LogManager.getLogger(SubDomainServiceImpl.class);

	@Autowired
	private SubDomainRepository subDomainRepository;

	@Autowired
	private DomainRepository domainRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private SurveyMapQuestionRepo surveyMapQuestionRepo;

	@Override
	public Integer createSubDomain(SubDomainRequest subDomainReq) {
		try {

			if (StringUtils.isValidObj(subDomainReq.getId()) && subDomainReq.getId() > 0) {
				// Update Domain Master API Logic
				Optional<SubDomain> subDomainOpt = subDomainRepository.findById(subDomainReq.getId());
				if (subDomainOpt.isPresent()) {
					
					List<SurveyMapQuestion> subD = surveyMapQuestionRepo.findBySubDomainId(subDomainReq.getId().intValue());
					if(!subD.isEmpty()) 
						return 6;	//Sub Domain is already assigned to the Survey with Map Question
					
					SubDomain subDomain = subDomainOpt.get();
					Optional<DomainMaster> domain = domainRepository.findById(subDomainReq.getDomainMaster().getId());
					if(!domain.isPresent()) {
						return 4;	//Domain Id is not present in Domain Master
					}
					Optional<SubDomain> subOpt = subDomainRepository.findBySubDomainCodeAndIdNot(subDomainReq.getSubDomainCode(),subDomainReq.getId());
					if(subOpt.isPresent()) {
						return 5;	//SubDomain Code already exists.
					}
					Optional<SubDomain> duplicateSubDomain = subDomainRepository
							.findByDomainMasterIdAndSubDomainNameIgnoreCaseAndIdNot(
									subDomainReq.getDomainMaster().getId(), subDomainReq.getSubDomainName(),
									subDomainReq.getId());
					if(duplicateSubDomain.isPresent()) {
						return 3;	//Record is duplicate.
					}
					subDomain.setSubDomainName(subDomainReq.getSubDomainName());
					subDomain.setSubDomainCode(subDomainReq.getSubDomainCode());
					subDomain.setDomainMaster(domain.get());
					subDomain.setStatus(subDomainReq.getStatus());
					subDomain.setUpdatedAt(new Date());
					subDomain.setUpdatedBy(subDomainReq.getLoggedInUserId());
					subDomainRepository.save(subDomain);
					return 1; // Updated Successfully.
				}
				return 2; // SubDomain Id is not present.

			} else {
				// Create Domain Master API Logic

				Optional<DomainMaster> domain = domainRepository.findById(subDomainReq.getDomainMaster().getId());
				if(!domain.isPresent()) {
					return 4;	//Domain Id is not present in Domain Master
				}
				
				Optional<SubDomain> subOpt = subDomainRepository.findBySubDomainCode(subDomainReq.getSubDomainCode());
				if(subOpt.isPresent()) {
					return 5;	//SubDomain Code already exists.
				}
				
				Optional<SubDomain> duplicateSubDomain = subDomainRepository
						.findByDomainMasterIdAndSubDomainNameIgnoreCase(
								subDomainReq.getDomainMaster().getId(), subDomainReq.getSubDomainName()
								);
				if(duplicateSubDomain.isPresent()) {
					return 3;	//Record is duplicate.
				}
				
				
				SubDomain subDomain = modelMapper.map(subDomainReq, SubDomain.class);
				subDomain.setCreatedAt(new Date());
				subDomain.setStatus(subDomainReq.getStatus());
				subDomain.setCreatedBy(subDomainReq.getLoggedInUserId());
				subDomain.setDomainMaster(domain.get());
				subDomainRepository.save(subDomain);
				return 1; // Created Successfully.
			}

		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public List<SubDomain> viewSubDomain(String sortByColumn, String sortDirection ) {
		try {
			String validSortColumn = validateSortColumn(sortByColumn);
	        String validSortDirection = validateSortDirection(sortDirection);
			
			List<SubDomain> subDomainsResponse = new ArrayList<SubDomain>();
			
			
			List<SubDomain> subDomains= subDomainRepository.findAll(Sort.by(Sort.Direction.fromString(validSortDirection), validSortColumn));
			
			for(SubDomain  subDomain : subDomains) {
				
				SubDomain subDomRes = new SubDomain();
				
				BeanUtils.copyProperties(subDomain, subDomRes);
				
				List<SurveyMapQuestion> subD = surveyMapQuestionRepo.findBySubDomainId(subDomain.getId());
				if(subD.isEmpty()) 
					subDomRes.setIsEditable(true);
				else
					subDomRes.setIsEditable(false);
				
				subDomainsResponse.add(subDomRes);
			}
			return subDomainsResponse;
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());

		}
	}
	private String validateSortDirection(String sortDirection) {
		return sortDirection;
	}

	private String validateSortColumn(String sortByColumn) {
		return sortByColumn;
	}
	@Override
	public Integer deleteSubDomain(DeleteRequestDto subDomainReq) {
		try {
			if (StringUtils.isValidObj(subDomainReq.getId()) && subDomainReq.getId() > 0) {
				Optional<SubDomain> subDomain = subDomainRepository.findById(subDomainReq.getId().intValue());
				if (subDomain.isPresent()) {
					
					List<SurveyMapQuestion> subD = surveyMapQuestionRepo.findBySubDomainId(subDomainReq.getId().intValue());
					if(!subD.isEmpty()) 
						return 4;	//Sub Domain is already assigned to the Survey with Map Question
					
					subDomain.get().setStatus(false);
					subDomain.get().setUpdatedAt(new Date());
					subDomain.get().setUpdatedBy(subDomainReq.getLoggedInUserId());
					subDomainRepository.save(subDomain.get());
					return 1; // Deleted Successfully.
				}
				return 2; // Level ID is not exist.
			}
			return 3; // Please provide valid Level ID.
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
}
