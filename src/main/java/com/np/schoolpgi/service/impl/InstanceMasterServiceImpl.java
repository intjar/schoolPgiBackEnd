package com.np.schoolpgi.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.InstanceMasterRepository;
import com.np.schoolpgi.dao.LevelMasterRepository;
import com.np.schoolpgi.dto.request.AddInstanceRequest;
import com.np.schoolpgi.dto.request.DeleteUpdateInstanceRequest;
import com.np.schoolpgi.dto.request.InstancesWithSchoolRequest;
import com.np.schoolpgi.dto.response.InstanceResponse;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.InstanceMaster;
import com.np.schoolpgi.model.LevelMaster;
import com.np.schoolpgi.service.InstanceMasterService;

@Service
public class InstanceMasterServiceImpl implements InstanceMasterService {

	@Autowired
	InstanceMasterRepository instanceMasterRepository;

	@Autowired
	private LevelMasterRepository levelMasterRepository;

	@Autowired
	private InstanceMasterRepository instanceRepository;

	final static Logger LOGGER = LogManager.getLogger(LevelMasterServiceImpl.class);

	@Override
	public Integer addInstance(AddInstanceRequest addInstanceRequest) {
		try {
			Optional<InstanceMaster> searchBycode = instanceMasterRepository
					.findByInstanceCode(addInstanceRequest.getInstanceCode());

			if (searchBycode.isPresent()) {
				return 2;
			}
			List<InstanceMaster> searchByName = instanceMasterRepository
					.findByInstanceNameIgnoreCase(addInstanceRequest.getInstanceName());

			if (searchByName != null) {
				for (InstanceMaster instanceMaster : searchByName) {
					if (instanceMaster.getParentInstanceId() == addInstanceRequest.getParentInstanceId())
						return 3;
				}

			}
			InstanceMaster instanceMaster = new InstanceMaster();
			instanceMaster.setInstanceName(addInstanceRequest.getInstanceName());
//			LevelMaster lm = new LevelMaster();
//			lm.setId(addInstanceRequest.getLevelId());
			instanceMaster.setLevel(addInstanceRequest.getLevelId());
			// instanceMaster.setLevelId(lm);
			instanceMaster.setParentInstanceId(addInstanceRequest.getParentInstanceId());
			instanceMaster.setStatus(addInstanceRequest.getStatus());
			instanceMaster.setCreatedBy(addInstanceRequest.getLoggedInUserId());
			instanceMaster.setInstanceCode(addInstanceRequest.getInstanceCode());
			instanceMaster.setCreatedAt(new Date());
			InstanceMaster save = instanceMasterRepository.save(instanceMaster);

			if (save == null) {
				return 0;
			} else {
				return 1;
			}
		} catch (Exception ex) {
			LOGGER.info("Something went wrong " + ex.getMessage());
			ex.printStackTrace();
			throw new SomethingWentWrongException(ex.getMessage());
		}

	}

	@Override
	public ResponseWithPagination getInstances(InstancesWithSchoolRequest instancesWithSchoolRequest, int pageNo,
			int pageSize, String sortDir, String sortBy, String searchKey) {
		try {
			List<InstanceMaster> list = new ArrayList<>();
//			Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
//	                : Sort.by(sortBy).ascending();
			Sort sort;
			if (sortByExistsInInstanceMaster(sortBy)) {
				sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
						: Sort.by(sortBy).ascending();
			} else {
				sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by("id").descending()
						: Sort.by("id").ascending();
			}

			Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
			Page<InstanceMaster> pageableData = null;

			ResponseWithPagination responseWithPagination = new ResponseWithPagination();

			if (instancesWithSchoolRequest.getIsDropDown()) {
				list = instanceMasterRepository.findAll();
			} else {
				if (instancesWithSchoolRequest.getWithSchool() == false) {
					pageableData = instanceMasterRepository.findInstanceWithoutSchool(searchKey, pageable);
					list = pageableData.getContent();
				} else {
					pageableData = instanceMasterRepository.findAllInstance(searchKey, pageable);
					list = pageableData.getContent();
				}
			}

//			
			if (list == null) {
				return null;
			} else {
				List<InstanceResponse> instanceList = new ArrayList<>();
				for (InstanceMaster i : list) {
					InstanceResponse instance = new InstanceResponse();
					instance.setId(i.getId());
					instance.setInstance(i.getInstanceName());
					instance.setInstanceCode(i.getInstanceCode());
					instance.setParentInstanceId(i.getParentInstanceId());
					Optional<LevelMaster> level = levelMasterRepository.findById(i.getLevel());
					if (level.isPresent()) {
						instance.setLevel(level.get().getLevelName());
						instance.setLevelId(level.get().getId());
						Optional<LevelMaster> parentlevel = levelMasterRepository
								.findById(level.get().getParentLevelId());
						if (parentlevel.isPresent()) {
							instance.setParentLevel(parentlevel.get().getLevelName());
							instance.setParentLevelId(parentlevel.get().getId());
						}
					}
					Optional<InstanceMaster> parentInstance = instanceMasterRepository
							.findById(i.getParentInstanceId());
					if (parentInstance.isPresent()) {
						instance.setParentInstance(parentInstance.get().getInstanceName());
					}
					instance.setStatus(i.getStatus());

					instanceList.add(instance);
				}
//			Collections.sort(instanceList, new InstanceViewComparator());
				responseWithPagination.setHttpStatus(HttpStatus.OK);
				responseWithPagination.setMessage(APPServiceCode.APP001.getStatusDesc());
				responseWithPagination.setMessageCode(APPServiceCode.APP001.getStatusCode());
				responseWithPagination.setResult(instanceList);
				responseWithPagination.setSuccess(true);

				if (!instancesWithSchoolRequest.getIsDropDown()) {
					responseWithPagination.setPageNo(pageableData.getNumber());
					responseWithPagination.setPageSize(pageableData.getSize());
					responseWithPagination.setTotalElements(pageableData.getTotalElements());
					responseWithPagination.setTotalPages(pageableData.getTotalPages());
					responseWithPagination.setLast(pageableData.isLast());
				}
				return responseWithPagination;
			}
		} catch (Exception ex) {
			LOGGER.error(ex);
			throw new SomethingWentWrongException(ex.getMessage());
		}

	}

	private boolean sortByExistsInInstanceMaster(String sortBy) {
		try {
			Field[] fields = InstanceMaster.class.getDeclaredFields();
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

	@Override
	public Boolean deleteInstance(Integer instanceId, Integer userId) {

		try {
			Optional<InstanceMaster> instance = instanceMasterRepository.findById(instanceId);

			if (instance.isPresent()) {
				instance.get().setStatus(false);
				instance.get().setUpdatedAt(new Date());
				instance.get().setUpdatedBy(userId);
				instanceMasterRepository.save(instance.get());
				return true;
			}
			return false;
		} catch (Exception ex) {
			LOGGER.info("Something went wrong " + ex.getMessage());
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	@Override
	public Integer updateInstance(DeleteUpdateInstanceRequest updateInstanceRequest) {
		try {
			Optional<InstanceMaster> searchBycode = instanceMasterRepository
					.findByInstanceCodeUpdate(updateInstanceRequest.getInstanceCode(), updateInstanceRequest.getId());

			if (searchBycode.isPresent()) {
				return 2;
			}
			List<InstanceMaster> searchByName = instanceMasterRepository
					.findByInstanceNameUpdate(updateInstanceRequest.getId(), updateInstanceRequest.getInstanceName());

			if (searchByName != null) {
				for (InstanceMaster instanceMaster : searchByName) {
					if (instanceMaster.getParentInstanceId() == updateInstanceRequest.getParentInstanceId())
						return 3;
				}

			}
			Optional<InstanceMaster> instanceOpt = instanceMasterRepository.findById(updateInstanceRequest.getId());
			if (instanceOpt.isPresent()) {
				InstanceMaster instance = instanceOpt.get();
				instance.setUpdatedAt(new Date());
				instance.setUpdatedBy(updateInstanceRequest.getLoggedInUserId());
				instance.setInstanceName(updateInstanceRequest.getInstanceName());
				instance.setLevel(updateInstanceRequest.getLevelId());
				instance.setParentInstanceId(updateInstanceRequest.getParentInstanceId());
				instance.setInstanceCode(updateInstanceRequest.getInstanceCode());
				instance.setStatus(updateInstanceRequest.getStatus());
				instanceMasterRepository.save(instance);
				return 1;
			} else {
				return 0;
			}
		} catch (Exception ex) {
			LOGGER.info("Something went wrong " + ex.getMessage());
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	@Override
	public List<InstanceMaster> childInstances(Integer id) {
		try {
			List<InstanceMaster> instanceMList = instanceRepository.findChildInstances();

			// Update Logic
			Optional<InstanceMaster> instanceDB = instanceRepository.findById(id);
			if (!instanceDB.isPresent()) {
				return null;
			}
			List<InstanceMaster> instancesOutput = new ArrayList<>();
			Queue<InstanceMaster> instanceMasters = new LinkedList<>();
			instanceMasters.add(instanceDB.get());
			while (!instanceMasters.isEmpty()) {
				InstanceMaster instance = instanceMasters.remove();
				if (instanceMList.isEmpty()) {
					break;
				}
				for (InstanceMaster i : instanceMList) {
					if (i.getParentInstanceId() == instance.getId() && !instancesOutput.contains(i)) {
						instanceMasters.add(i);
						instancesOutput.add(i);
					}
				}
				instanceMList.remove(instance);
			}
			return instancesOutput;
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}
