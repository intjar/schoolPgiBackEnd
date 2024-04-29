package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.RoleReqDto;
import com.np.schoolpgi.model.Roles;

public interface RoleService {
	
	Integer createRole(RoleReqDto roleRequest);
	Integer updateRole(RoleReqDto roleRequest);
	Integer deleteRole(DeleteRequestDto roleRequest);
	List<Roles> viewRole(String sortByColumn, String sortDirection);
}
