package com.np.schoolpgi.service;

import com.np.schoolpgi.dto.request.DeleteSchoolInstanceReqDto;
import com.np.schoolpgi.dto.request.SchoolMasterRequest;
import com.np.schoolpgi.dto.request.ViewByListIds;
import com.np.schoolpgi.dto.response.BlockInstancResponse;
import com.np.schoolpgi.dto.response.ResponseWithPagination;

public interface SchoolService {
	Integer createSchool(SchoolMasterRequest school);
	ResponseWithPagination viewSchool(int pageNo, int pageSize, String sortDir, String sortBy, String searchKey);
	Integer deleteSchool(DeleteSchoolInstanceReqDto school);
	BlockInstancResponse viewBlockLevelInstance(ViewByListIds viewByListIds);
}
