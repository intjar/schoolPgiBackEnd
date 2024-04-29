package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.SubDomainRequest;
import com.np.schoolpgi.model.SubDomain;

public interface SubDomainService {
	
	Integer createSubDomain(SubDomainRequest subDomainReq);
	List<SubDomain> viewSubDomain(String sortByColumn, String sortDirection );
	Integer deleteSubDomain(DeleteRequestDto subDomainReq);
}
