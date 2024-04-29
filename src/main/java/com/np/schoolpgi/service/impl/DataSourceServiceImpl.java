package com.np.schoolpgi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.DataSourceRepository;
import com.np.schoolpgi.dto.request.DataSourceRequest;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.DataSource;
import com.np.schoolpgi.service.DataSourceService;
import com.np.schoolpgi.util.StringUtils;

@Service
public class DataSourceServiceImpl implements DataSourceService {

	final static Logger LOGGER = LogManager.getLogger(DataSourceServiceImpl.class);

	@Autowired
	private DataSourceRepository dataSourceRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Integer createDataSource(DataSourceRequest dataReq) {
		try {
			if (StringUtils.isValidObj(dataReq.getId()) && dataReq.getId() > 0) {
				// Update DataSource API Logic

				Optional<DataSource> dataSourceOpt = dataSourceRepo.findById(dataReq.getId());
				if (dataSourceOpt.isPresent()) {
					DataSource dataSource = dataSourceOpt.get();
					Optional<DataSource> findByCodeAndIdNot = dataSourceRepo.findByCodeAndIdNot(dataReq.getCode(), dataReq.getId());
					if(findByCodeAndIdNot.isPresent()) {
						return 4;	//Code already exist.
					}
					Optional<DataSource> findByNameAndIdNot = dataSourceRepo.findByNameIgnoreCaseAndIdNot(dataReq.getName(), dataReq.getId());
					if(findByNameAndIdNot.isPresent()) {
						return 3;	//Name already exist.
					}
					
					dataSource.setCode(dataReq.getCode());
					dataSource.setName(dataReq.getName());
					dataSource.setStatus(dataReq.getStatus());
					dataSource.setUpdatedAt(new Date());
					dataSource.setUpdatedBy(dataReq.getLoggedInUserId());
//					if(StringUtils.isValidObj(dataReq.getUrl()) && StringUtils.isNotEmpty(dataReq.getUrl())) {
						dataSource.setUrl(dataReq.getUrl());
//					}
					dataSourceRepo.save(dataSource);
					return 1; // Updated Successfully.
				}

				return 2; // Source ID is not exist.
			} else {

				// Create DataSource API Logic

				if (StringUtils.isValidObj(dataReq.getName())) {
					Optional<DataSource> dataSourceByName = dataSourceRepo.findByNameIgnoreCase(dataReq.getName());
					if (dataSourceByName.isPresent()) {
						return 3; // Source name is already exist.
					}
					if (StringUtils.isValidObj(dataReq.getCode())) {
						Optional<DataSource> dataSourceByCode = dataSourceRepo.findByCode(dataReq.getCode());
						if(dataSourceByCode.isPresent()) {
							return 4;	//Source Code is already exist.
						}
						
						DataSource dataSource = modelMapper.map(dataReq, DataSource.class);
						dataSource.setCreatedAt(new Date());
						dataSource.setCreatedBy(dataReq.getLoggedInUserId());
						dataSource.setStatus(dataReq.getStatus());
						
						dataSourceRepo.save(dataSource);
						return 1;	//Created Successfully.
					}
					return 5;	//Please enter valid source code 

				}
				return 6;	//Please enter valid source name
			}

		}catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public List<DataSource> viewDataSource(String sortByColumn ,String sortDirection) {
        
		try {
			String validSortColumn = validateSortColumn(sortByColumn);
	        String validSortDirection = validateSortDirection(sortDirection);
			return dataSourceRepo.findAll(Sort.by(Sort.Direction.fromString(validSortDirection), validSortColumn));
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
	public Integer deleteDataSource(DeleteRequestDto dataReq) {
		try {
			if (StringUtils.isValidObj(dataReq.getId()) && dataReq.getId() > 0) {
				
				Optional<DataSource> dataSourceOpt = dataSourceRepo.findById(dataReq.getId().intValue());
				if (dataSourceOpt.isPresent()) {
					dataSourceOpt.get().setStatus(false);
					dataSourceOpt.get().setUpdatedAt(new Date());;
					dataSourceOpt.get().setUpdatedBy(dataReq.getLoggedInUserId());
					dataSourceRepo.save(dataSourceOpt.get());
					return 1;	//DELETED Successfully.
				}
				return 2;	//Source ID is not exist.
			}
			return 3;	//Please enter valid source ID.
			
		} catch (Exception e) {

			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}
