package com.np.schoolpgi.service;

import org.springframework.http.ResponseEntity;

import com.np.schoolpgi.dto.request.RoleLinkMappingUpdateRequest;
import com.np.schoolpgi.dto.request.ViewReqById;

public interface ManageSettingService {

	ResponseEntity<?> manageStting(Integer roleid) throws Exception;
	String manageSttingUpdateLinks(RoleLinkMappingUpdateRequest roleLinkMappingUpdateRequest) throws Exception;
	ResponseEntity<?> getRoleLinks(ViewReqById byId) throws Exception;

}
