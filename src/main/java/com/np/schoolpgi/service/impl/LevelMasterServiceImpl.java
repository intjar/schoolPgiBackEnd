package com.np.schoolpgi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.InstanceMasterRepository;
import com.np.schoolpgi.dao.LevelMasterRepository;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.LevelMasterRequest;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.InstanceMaster;
import com.np.schoolpgi.model.LevelMaster;
import com.np.schoolpgi.service.LevelMasterService;
import com.np.schoolpgi.util.StringUtils;

@Service
public class LevelMasterServiceImpl implements LevelMasterService {

	final static Logger LOGGER = LogManager.getLogger(LevelMasterServiceImpl.class);

	@Autowired
	private LevelMasterRepository levelRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private InstanceMasterRepository instanceMasterRepository;

	@Override
	public Integer createdLevel(LevelMasterRequest levelRequest) {

		try {
			if (StringUtils.isValidObj(levelRequest.getId()) && levelRequest.getId() > 0) {
				// Update Logic
				Optional<LevelMaster> levelDB = levelRepository.findById(levelRequest.getId());
				if (levelDB.isPresent()) {

					List<InstanceMaster> instanceMasters = instanceMasterRepository.findByLevel(levelRequest.getId());

					if (!instanceMasters.isEmpty()) {
						return 6;
					}

					Optional<LevelMaster> levelByName = levelRepository
							.findByLevelNameIgnoreCaseAndIdNot(levelRequest.getLevelName(), levelRequest.getId());

					if (levelByName.isPresent()) {
						return 3;
					}

					Optional<LevelMaster> levelByCode = levelRepository
							.findByLevelCodeAndIdNot(levelRequest.getLevelCode(), levelRequest.getId());

					if (levelByCode.isPresent()) {
						return 4;
					}

					LevelMaster level = levelDB.get();

					level.setLevelCode(levelRequest.getLevelCode());
					level.setLevelName(levelRequest.getLevelName());
					level.setParentLevelId(levelRequest.getParentLevelId());
					level.setRoleIds(levelRequest.getRoleIds());
					level.setStatus(levelRequest.getStatus());
					level.setUpdatedAt(new Date());
					level.setUpdatedBy(levelRequest.getLoggedInUserId());
					levelRepository.save(level);
					return 1; // Updated Successfully

				} else {
					return 2; // Source ID is not present
				}

			} else {
				// CreateLevel API Logic -----
				Optional<LevelMaster> findByLevelName = levelRepository
						.findByLevelNameIgnoreCase(levelRequest.getLevelName());
				if (findByLevelName.isPresent()) {
					return 3; // LevelName is already exist.
				}
				Optional<LevelMaster> findByLevelCode = levelRepository.findByLevelCode(levelRequest.getLevelCode());

				if (findByLevelCode.isPresent()) {
					return 4; // LevelCode is already exist.
				}

				LevelMaster levelMaster = modelMapper.map(levelRequest, LevelMaster.class);
				levelMaster.setCreatedAt(new Date());
				levelMaster.setStatus(levelRequest.getStatus());
				levelMaster.setCreatedBy(levelRequest.getLoggedInUserId());
				levelRepository.save(levelMaster);
				return 5; // Created Successfully
			}

		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// --------------------------View Level------------------------------------

	@Override
	public String viewLevelMaster(String isDropDown) {
		try {
			return levelRepository.getLevelMasterData(isDropDown);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public Integer deleteLevel(DeleteRequestDto levelRequest) {
		try {
			if (StringUtils.isValidObj(levelRequest.getId()) && levelRequest.getId() > 0) {
				Optional<LevelMaster> level = levelRepository.findById(levelRequest.getId().intValue());
				if (level.isPresent()) {

					List<InstanceMaster> instanceMasters = instanceMasterRepository.findByLevel(levelRequest.getId().intValue());

					if (!instanceMasters.isEmpty()) {
						return 4;
					}
					level.get().setStatus(false);
					level.get().setUpdatedAt(new Date());
					level.get().setUpdatedBy(levelRequest.getLoggedInUserId());
					levelRepository.save(level.get());
					return 1; // Deleted Successfully.
				}
				return 2; // Level ID is not exist.
			}
			return 3; // Please provide valid Level ID.
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public List<LevelMaster> childLevels(Integer l) {
		// TODO Auto-generated method stub

		try {
			List<LevelMaster> levelMList = levelRepository.findAllChildLevel();

			// Update Logic
			Optional<LevelMaster> levelDB = levelRepository.findById(l);
			if (!levelDB.isPresent()) {
				return null;
			}
			List<LevelMaster> levelsOutput = new ArrayList<>();
			Queue<LevelMaster> levelMasters = new LinkedList<>();
			levelMasters.add(levelDB.get());
			while (!levelMasters.isEmpty()) {
				LevelMaster level = levelMasters.remove();
				if (levelMList.isEmpty()) {
					break;
				}
				for (LevelMaster i : levelMList) {
					if (i.getParentLevelId() == level.getId() && !levelsOutput.contains(i)) {
						levelMasters.add(i);
						levelsOutput.add(i);
					}
				}
				levelMList.remove(level);
			}
			return levelsOutput;
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}
