package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.DomainMasterRequest;
import com.np.schoolpgi.model.DomainMaster;

public interface DomainService {

	Integer createDomain(DomainMasterRequest domainRequest);
	List<DomainMaster> viewDomain(String sortByColumn,String sortDirection);
	Integer deleteDomain(DeleteRequestDto domainRequest);
}
