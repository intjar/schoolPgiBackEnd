package com.np.schoolpgi.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.constants.AppConstants;
import com.np.schoolpgi.dao.InstanceMasterRepository;
import com.np.schoolpgi.dao.LevelMasterRepository;
import com.np.schoolpgi.dao.SchoolRepository;
import com.np.schoolpgi.dto.request.DeleteSchoolInstanceReqDto;
import com.np.schoolpgi.dto.request.SchoolMasterRequest;
import com.np.schoolpgi.dto.request.ViewByListIds;
import com.np.schoolpgi.dto.response.BlockInstancResponse;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.dto.response.SchoolResponse;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.InstanceMaster;
import com.np.schoolpgi.model.LevelMaster;
import com.np.schoolpgi.model.SchoolMaster;
import com.np.schoolpgi.service.SchoolService;
import com.np.schoolpgi.util.StringUtils;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {
	final static Logger LOGGER = LogManager.getLogger(SchoolServiceImpl.class);

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private LevelMasterRepository levelRepository;

	@Autowired
	private InstanceMasterRepository instanceMasterRepository;

	@Autowired
	private ModelMapper modelMapper;

	// CREATE School
	@Override
	public Integer createSchool(SchoolMasterRequest schoolRequest) {

		try {
			if (StringUtils.isValidObj(schoolRequest.getId()) && schoolRequest.getId() > 0) {
				// Update School API Logic
				Optional<SchoolMaster> school = schoolRepository.findById(schoolRequest.getId());
				if (school.isPresent()) {

					SchoolMaster schoolMaster = school.get();

					Optional<InstanceMaster> instance = instanceMasterRepository
							.findById(schoolRequest.getBlockLevelInstanceId());
					if (!instance.isPresent()) {
						return 6; // Block Level Instance id is not present in Instance Master
					}

					Optional<SchoolMaster> schoolByUdiseCode = schoolRepository
							.findByUdiseCodeAndIdNot(schoolRequest.getUdiseCode(), schoolRequest.getId());
					if (schoolByUdiseCode.isPresent()) {
						return 3;
					}
					
					SchoolMaster sch = schoolRepository.findBySchoolNameIgnoreCaseAndIdNot(schoolRequest.getSchoolName(),schoolRequest.getId());
					if(sch!=null) {
						return 8;	//School Name already exists.
					}

					Optional<InstanceMaster> findByInstanceCode = instanceMasterRepository
							.findById(schoolRequest.getInstanceMaster().getId());
					if (!findByInstanceCode.isPresent()) {
						return 7; // Instance ID does not exist in InstanceMaster
					}
					findByInstanceCode.get().setInstanceName(schoolRequest.getSchoolName());
					findByInstanceCode.get().setParentInstanceId(schoolRequest.getBlockLevelInstanceId());
					findByInstanceCode.get().setInstanceCode(Long.valueOf(schoolRequest.getUdiseCode()));
					
					// Find School Level Id
					Optional<LevelMaster> findByLevelName = levelRepository
							.findByLevelNameIgnoreCase(AppConstants.SCHOOL_LEVEL_NAME);
					if (findByLevelName.isPresent()) {
						findByInstanceCode.get().setLevel(findByLevelName.get().getId());
					} else {
						return 7; // School Level Id does not exist.
					}

					findByInstanceCode.get().setUpdatedAt(new Date());
					findByInstanceCode.get().setUpdatedBy(schoolRequest.getLoggedInUserId());

					if (schoolRequest.getStatus() == 0)
						findByInstanceCode.get().setStatus(false);
					else
						findByInstanceCode.get().setStatus(true);
					instanceMasterRepository.save(findByInstanceCode.get());
					schoolMaster.setLevelMaster(schoolRequest.getLevelMaster());
					schoolMaster.setInstanceMaster(schoolRequest.getInstanceMaster());
					schoolMaster.setBlockLevelInstanceId(schoolRequest.getBlockLevelInstanceId());
					schoolMaster.setSchoolName(schoolRequest.getSchoolName());
					schoolMaster.setStatus(schoolRequest.getStatus());
					schoolMaster.setUdiseCode(schoolRequest.getUdiseCode());
					schoolMaster.setUpdatedAt(new Date());
					schoolMaster.setUpdatedBy(schoolRequest.getLoggedInUserId());
					schoolRepository.save(schoolMaster);
					return 1; // Updated Successfully.
				}
				return 2; // Source id is not exist.

			} else {

				// Create School API Logic
				if (StringUtils.isValidObj(schoolRequest.getUdiseCode()) && schoolRequest.getUdiseCode().length() > 0) {
					Optional<SchoolMaster> school = schoolRepository.findByUdiseCode(schoolRequest.getUdiseCode());
					if (school.isPresent()) {
						return 3; // UDISE CODE is already exist.
					} else {
						Optional<InstanceMaster> instance = instanceMasterRepository
								.findById(schoolRequest.getBlockLevelInstanceId());
						if (!instance.isPresent()) {
							return 6; // Block Level Instance id is not present in Instance Master
						}
					SchoolMaster sch = schoolRepository.findBySchoolNameIgnoreCase(schoolRequest.getSchoolName());
					if(sch!=null) {
						return 8;	//School Name already exists.
					}
						
						InstanceMaster instanceMaster = new InstanceMaster();

						instanceMaster.setInstanceCode(Long.parseLong(schoolRequest.getUdiseCode()));
						instanceMaster.setInstanceName(schoolRequest.getSchoolName());

						// Find School Level Id
						Optional<LevelMaster> findByLevelName = levelRepository
								.findByLevelNameIgnoreCase(AppConstants.SCHOOL_LEVEL_NAME);
						if (findByLevelName.isPresent()) {
							instanceMaster.setLevel(findByLevelName.get().getId());
						} else {
							return 7; // School Level Id does not exist.
						}
						instanceMaster.setParentInstanceId(schoolRequest.getBlockLevelInstanceId());
						if (schoolRequest.getStatus() == 0)
							instanceMaster.setStatus(false);
						else
							instanceMaster.setStatus(true);
						instanceMaster.setCreatedAt(new Date());
						instanceMaster.setCreatedBy(schoolRequest.getLoggedInUserId());

						InstanceMaster savedInstance = instanceMasterRepository.save(instanceMaster);

						SchoolMaster schoolMaster = modelMapper.map(schoolRequest, SchoolMaster.class);
						schoolMaster.setInstanceMaster(instance.get());

						schoolMaster.setStatus(schoolRequest.getStatus());
						schoolMaster.setCreatedAt(new Date());
						schoolMaster.setCreatedBy(schoolRequest.getLoggedInUserId());
						schoolMaster.setInstanceMaster(savedInstance);
						schoolRepository.save(schoolMaster);

						return 4; // School Created Successfully.
					}

				}
				return 5; // Please provide valid UDISE CODE

			}

		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// VIEW School
	@Override
	public ResponseWithPagination viewSchool(int pageNo, int pageSize, String sortDir, String sortBy, String searchKey) {
		try {
			ResponseWithPagination responseWithPagination = new ResponseWithPagination();
//			Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
//            : Sort.by(sortBy).ascending();
			
			Sort sort;
			if (sortByExistsInSchoolMaster(sortBy)) {
				sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
						: Sort.by(sortBy).ascending();
			}else {
				sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by("id").descending()
						: Sort.by("id").ascending();
			}
			Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
			Page<SchoolMaster> pageableData=null;
			
			List<SchoolResponse> schoolResponses = new ArrayList<SchoolResponse>();
			
			pageableData = schoolRepository.findAllSchools(pageable,searchKey);
			
			List<SchoolMaster> schoolsWithInstance = new ArrayList<>();
			schoolsWithInstance = pageableData.getContent();
			
			for(SchoolMaster schools : schoolsWithInstance) {
				
				SchoolResponse schoolResponse = new SchoolResponse();
				
				SchoolMaster schoolMaster = new SchoolMaster();
				
				schoolMaster.setId(schools.getId());
				schoolMaster.setSchoolName(schools.getSchoolName());
				schoolMaster.setUdiseCode(schools.getUdiseCode());
				schoolMaster.setStatus(schools.getStatus());
				schoolMaster.setCreatedAt(schools.getCreatedAt());
				schoolMaster.setCreatedBy(schools.getCreatedBy());
				schoolMaster.setUpdatedAt(schools.getUpdatedAt());
				schoolMaster.setUpdatedBy(schools.getUpdatedBy());
				
				LevelMaster levelMaster = new LevelMaster();
				levelMaster.setId(schools.getLevelMaster().getId());
				levelMaster.setLevelName(schools.getLevelMaster().getLevelName());
				levelMaster.setLevelCode(schools.getLevelMaster().getLevelCode());
			
				
				
				InstanceMaster levelInstanceMaster = new InstanceMaster();
				Optional<InstanceMaster> blockLevelInstance = instanceMasterRepository.findById(schools.getBlockLevelInstanceId());
				levelInstanceMaster.setId(schools.getBlockLevelInstanceId());
				levelInstanceMaster.setInstanceName(blockLevelInstance.get().getInstanceName());
				levelInstanceMaster.setInstanceCode(blockLevelInstance.get().getInstanceCode());
				
				
				InstanceMaster instanceMaster = new InstanceMaster();
				instanceMaster.setId(schools.getInstanceMaster().getId());
				instanceMaster.setInstanceName(schools.getInstanceMaster().getInstanceName());
				instanceMaster.setInstanceCode(schools.getInstanceMaster().getInstanceCode());
				
				schoolResponse.setLevelMaster(levelMaster);
				schoolResponse.setBlockLevelInstanceMaster(levelInstanceMaster);
				schoolResponse.setInstanceMaster(instanceMaster);
				schoolResponse.setSchoolMaster(schoolMaster);
				schoolResponses.add(schoolResponse);
				
			}
			responseWithPagination.setHttpStatus(HttpStatus.OK);
			responseWithPagination.setLast(pageableData.isLast());
			responseWithPagination.setMessage(APPServiceCode.APP001.getStatusDesc());
			responseWithPagination.setMessageCode(APPServiceCode.APP001.getStatusCode());
			responseWithPagination.setPageNo(pageableData.getNumber());
			responseWithPagination.setPageSize(pageableData.getSize());
			responseWithPagination.setResult(schoolResponses);
			responseWithPagination.setSuccess(true);
			responseWithPagination.setTotalElements(pageableData.getTotalElements());
			responseWithPagination.setTotalPages(pageableData.getTotalPages());
			return responseWithPagination;
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	
	private boolean sortByExistsInSchoolMaster(String sortBy) {
		try {
			Field[] fields = SchoolMaster.class.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals(sortBy)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	// DELETE School API
	@Override
	public Integer deleteSchool(DeleteSchoolInstanceReqDto schoolRequest) {

		try {
			if (StringUtils.isValidObj(schoolRequest.getId()) && schoolRequest.getId() > 0
					&& StringUtils.isValidObj(schoolRequest.getInstanceId()) && schoolRequest.getInstanceId() > 0) {
				
				Optional<InstanceMaster> instance = instanceMasterRepository.findById(schoolRequest.getInstanceId().intValue());
				if(instance.isPresent()) {
					instance.get().setStatus(false);
					instance.get().setUpdatedAt(new Date());
					instance.get().setUpdatedBy(schoolRequest.getLoggedInUserId());
					instanceMasterRepository.save(instance.get());
				}else {
					return 4;	//Instance Id does not exist in Instance Master
				}
				Optional<SchoolMaster> school = schoolRepository.findById(schoolRequest.getId());
				if (school.isPresent()) {
					school.get().setStatus(0);
					school.get().setUpdatedAt(new Date());
					school.get().setUpdatedBy(schoolRequest.getLoggedInUserId());
					schoolRepository.save(school.get());
					return 1; // Deleted Successfully.
				}
				return 2; // School ID is not exist.
			}
			return 3; // Please provide valid school ID.
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// VIEW Block-Level-Instance
	@Override
	public BlockInstancResponse viewBlockLevelInstance(ViewByListIds viewByListIds) {

		try {
			BlockInstancResponse blockInstancResponse = new BlockInstancResponse();

			APPServiceCode appServiceCode = null;

			if (StringUtils.isValidObj(viewByListIds)) {
				List<InstanceMaster> blockInstances = instanceMasterRepository.findByLevelIn(viewByListIds.getListIds());
				if (!blockInstances.isEmpty()) {
					appServiceCode = APPServiceCode.APP001;
					blockInstancResponse.setSuccess(true);
					blockInstancResponse.setAppServiceCode(appServiceCode);
					blockInstancResponse.setResult(blockInstances);
					return blockInstancResponse;
				}
				appServiceCode = APPServiceCode.APP996; // Source ID is not exist.
				blockInstancResponse.setSuccess(false);
				blockInstancResponse.setAppServiceCode(appServiceCode);
				blockInstancResponse.setResult(null);
				return blockInstancResponse;
			} else {
				appServiceCode = APPServiceCode.APP507; // Please provide valid source id
				blockInstancResponse.setSuccess(false);
				blockInstancResponse.setAppServiceCode(appServiceCode);
				blockInstancResponse.setResult(null);
				return blockInstancResponse;
			}
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

}
