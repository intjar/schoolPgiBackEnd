package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.request.AddInstanceRequest;
import com.np.schoolpgi.dto.request.DeleteUpdateInstanceRequest;
import com.np.schoolpgi.dto.request.InstancesWithSchoolRequest;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.model.InstanceMaster;

public interface InstanceMasterService {

	public Integer addInstance(AddInstanceRequest addInstanceRequest);
	public ResponseWithPagination getInstances(InstancesWithSchoolRequest instancesWithSchoolRequest,int pageNo, int pageSize, String sortDir, String sortBy, String searchKey);
	public Boolean deleteInstance(Integer instanceId,Integer userId);
	public Integer updateInstance(DeleteUpdateInstanceRequest updateInstanceRequest );
	List<InstanceMaster> childInstances(Integer l);
}
