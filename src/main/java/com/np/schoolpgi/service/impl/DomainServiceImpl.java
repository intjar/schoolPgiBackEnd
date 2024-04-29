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

import com.np.schoolpgi.dao.DomainRepository;
import com.np.schoolpgi.dao.SurveyMapQuestionRepo;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.DomainMasterRequest;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.DomainMaster;
import com.np.schoolpgi.model.SurveyMapQuestion;
import com.np.schoolpgi.service.DomainService;
import com.np.schoolpgi.util.StringUtils;

@Service
public class DomainServiceImpl implements DomainService {

	final static Logger LOGGER = LogManager.getLogger(DomainServiceImpl.class);

	@Autowired
	private DomainRepository domainRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SurveyMapQuestionRepo surveyMapQuestionRepo;

	// Create Domain API
	@Override
	public Integer createDomain(DomainMasterRequest domainRequest) {
		try {

			if (StringUtils.isValidObj(domainRequest.getId())) {
				// Update Domain Master API Logic
				Optional<DomainMaster> domain = domainRepository.findById(domainRequest.getId());
				if (domain.isPresent()) {

					List<SurveyMapQuestion> surveyMapQuestions = surveyMapQuestionRepo
							.findByDomainMasterId(domainRequest.getId());

					if (!surveyMapQuestions.isEmpty()) {
						return 6; // Domain is already assigned to the Survey
					}

					Optional<DomainMaster> domainByName = domainRepository
							.findByDomainNameIgnoreCaseAndIdNot(domainRequest.getDomainName(), domainRequest.getId());

					if (domainByName.isPresent()) {
						return 3;
					}

					Optional<DomainMaster> domainByCode = domainRepository
							.findByDomainCodeAndIdNot(domainRequest.getDomainCode(), domainRequest.getId());

					if (domainByCode.isPresent()) {
						return 4;
					}

					DomainMaster domainMaster = domain.get();
					domainMaster.setDomainName(domainRequest.getDomainName());
					domainMaster.setDomainCode(domainRequest.getDomainCode());
					domainMaster.setStatus(domainRequest.getStatus());
					domainMaster.setUpdatedAt(new Date());
					domainMaster.setUpdatedBy(domainRequest.getLoggedInUserId());
					domainRepository.save(domainMaster);
					return 1; // Updated Successfully.
				}
				return 2; // Source Id is not present.

			} else {
				// Create Domain Master API Logic

				Optional<DomainMaster> findByDomainName = domainRepository
						.findByDomainNameIgnoreCase(domainRequest.getDomainName());
				if (findByDomainName.isPresent()) {
					return 3; // Domain Name is already exist.
				}
				Optional<DomainMaster> findByDomainCode = domainRepository
						.findByDomainCode(domainRequest.getDomainCode());
				if (findByDomainCode.isPresent()) {
					return 4; // Domain Code is already exist.
				}
				DomainMaster domainMaster = modelMapper.map(domainRequest, DomainMaster.class);
				domainMaster.setCreatedAt(new Date());
				domainMaster.setStatus(domainRequest.getStatus());
				domainMaster.setCreatedBy(domainRequest.getLoggedInUserId());
				domainRepository.save(domainMaster);
				return 5; // Created Successfully.
			}

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	
	// View Domain API
	@Override
	public List<DomainMaster> viewDomain(String sortByColumn,String sortDirection) {

		try {
			List<DomainMaster> domainsResponse = new ArrayList<DomainMaster>();
			
			 String validSortColumn = validateSortColumn(sortByColumn);
	  	     String validSortDirection = validateSortDirection(sortDirection);
	  	     
			List<DomainMaster> domains = domainRepository.findAll(
					Sort.by(Sort.Direction.fromString(validSortDirection), validSortColumn));

			for (DomainMaster domainMaster : domains) {
				DomainMaster dm = new DomainMaster();
				BeanUtils.copyProperties(domainMaster, dm);

				List<SurveyMapQuestion> surveyMapQuestions = surveyMapQuestionRepo
						.findByDomainMasterId(domainMaster.getId());
				if (surveyMapQuestions.isEmpty())
					dm.setIsEditable(true);
				else
					dm.setIsEditable(false);

				domainsResponse.add(dm);
			}
			return domainsResponse;
		} catch (Exception e) {
			LOGGER.error(e);
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
	public Integer deleteDomain(DeleteRequestDto domainRequest) {

		try {
			if (StringUtils.isValidObj(domainRequest.getId()) && domainRequest.getId() > 0) {
				Optional<DomainMaster> domain = domainRepository.findById(domainRequest.getId().intValue());
				if (domain.isPresent()) {

					List<SurveyMapQuestion> surveyMapQuestions = surveyMapQuestionRepo
							.findByDomainMasterId(domainRequest.getId().intValue());

					if (!surveyMapQuestions.isEmpty()) {
						return 4; // Domain is already assigned to the Survey
					}

					domain.get().setStatus(false);
					domain.get().setUpdatedAt(new Date());
					domain.get().setUpdatedBy(domainRequest.getLoggedInUserId());
					domainRepository.save(domain.get());
					return 1; // Deleted Successfully.
				}
				return 2; // Level ID is not exist.
			}
			return 3; // Please provide valid Level ID.
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}
