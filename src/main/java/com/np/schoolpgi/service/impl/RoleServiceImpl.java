package com.np.schoolpgi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.RoleRepository;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.RoleReqDto;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.Roles;
import com.np.schoolpgi.model.User;
import com.np.schoolpgi.service.RoleService;
import com.np.schoolpgi.util.StringUtils;

@Service
public class RoleServiceImpl implements RoleService {

	final static Logger LOGGER = LogManager.getLogger(RoleServiceImpl.class);

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Integer createRole(RoleReqDto roleRequest) {

		try {

			Optional<Roles> role = roleRepository.findByNameIgnoreCase(roleRequest.getName());
			if (role.isPresent()) {
				return 2; // Source name is already exist.
			}
			Optional<Roles> roleByCode = roleRepository.findByCode(roleRequest.getCode());
			if (roleByCode.isPresent()) {
				return 3; // Source Code is already exist.
			}

			Roles roles = modelMapper.map(roleRequest, Roles.class);

			roles.setCreatedAt(new Date());
			roles.setStatus(roleRequest.getStatus());
			roles.setCreatedBy(roleRequest.getLoggedInUserId());
			roleRepository.save(roles);
			return 1;

		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	
	@Override
	public List<Roles> viewRole(String sortByColumn, String sortDirection) {
	    try {
	        List<Roles> rolesResponse = new ArrayList<Roles>();

	        String validSortColumn = validateSortColumn(sortByColumn);
	        String validSortDirection = validateSortDirection(sortDirection);

	        List<Roles> findAll = roleRepository.findAll(
	                Sort.by(Sort.Direction.fromString(validSortDirection), validSortColumn));

	        for (Roles roleDb : findAll) {
	            Roles roleResp = new Roles();

	            List<Optional<User>> user = userRepository.findByRoleIdId(roleDb.getId());
	            roleResp.setId(roleDb.getId());
	            roleResp.setName(roleDb.getName());
	            roleResp.setCode(roleDb.getCode());
	            roleResp.setStatus(roleDb.getStatus());
	            roleResp.setCreatedAt(roleDb.getCreatedAt());
	            roleResp.setCreatedBy(roleDb.getCreatedBy());
	            roleResp.setUpdatedAt(roleDb.getUpdatedAt());
	            roleResp.setUpdatedBy(roleDb.getUpdatedBy());

	            if (user.isEmpty())
	                roleResp.setIsEditable(true);
	            else
	                roleResp.setIsEditable(false);

	            rolesResponse.add(roleResp);
	        }
	        return rolesResponse;
	    } catch (Exception e) {
	        LOGGER.error(e);
	        throw new SomethingWentWrongException(e.getMessage());
	    }
	}

	private String validateSortColumn(String sortByColumn) {
	    return sortByColumn;
	}

	private String validateSortDirection(String sortDirection) {
	    return sortDirection;
	}

	@Override
	public Integer updateRole(RoleReqDto roleRequest) {
		try {

			if (StringUtils.isValidObj(roleRequest.getId()) && roleRequest.getId() > 0) {
				Optional<Roles> role = roleRepository.findById(roleRequest.getId());
				if (role.isPresent()) {
					
					List<Optional<User>> user = userRepository.findByRoleIdId(roleRequest.getId());
					if(!user.isEmpty()) {
						return 6;
					}
					
					Optional<Roles> roleByNameNotId = roleRepository.findByNameIgnoreCaseAndIdNot(roleRequest.getName(),
							roleRequest.getId());
					if (roleByNameNotId.isPresent()) {
						return 2; // Source Name is already exist.
					}
					Optional<Roles> roleByCodeNotId = roleRepository.findByCodeAndIdNot(roleRequest.getCode(),
							roleRequest.getId());
					if (roleByCodeNotId.isPresent()) {
						return 3; // Source Code is already exist.
					}
					Roles roles = role.get();
					roles.setName(roleRequest.getName());
					roles.setCode(roleRequest.getCode());
					roles.setStatus(roleRequest.getStatus());
					roles.setUpdatedAt(new Date());
					roles.setUpdatedBy(roleRequest.getLoggedInUserId());
					roleRepository.save(roles);
					return 1; // Successfully updated.
				}
				return 4; // Role Id is not present.
			}
			return 5; // Please provide valid ID.

		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public Integer deleteRole(DeleteRequestDto roleRequest) {
		try {

			if (StringUtils.isValidObj(roleRequest.getId()) && roleRequest.getId() > 0) {
				Optional<Roles> role = roleRepository.findById(roleRequest.getId().intValue());

				if (role.isPresent()) {

					List<Optional<User>> user = userRepository.findByRoleIdId(roleRequest.getId().intValue());

					if (!user.isEmpty()) {
						return 4;
					}
					
					role.get().setStatus(false);
					role.get().setUpdatedAt(new Date());
					role.get().setUpdatedBy(roleRequest.getLoggedInUserId());
					roleRepository.save(role.get());
					return 1;
				}
				return 2;

			}
			return 3;
		} catch (Exception e) {

			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}
