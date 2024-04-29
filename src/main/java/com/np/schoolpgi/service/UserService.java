package com.np.schoolpgi.service;

import com.np.schoolpgi.dto.request.DeleteUpdateUserRequest;
import com.np.schoolpgi.dto.request.UserCreateRequest;
import com.np.schoolpgi.dto.request.ViewListRequest;
import com.np.schoolpgi.dto.response.ResponseWithPagination;

public interface UserService {
	
 
	Integer createUser(UserCreateRequest user);
	ResponseWithPagination usersList(ViewListRequest viewListRequest, int pageNo, int pageSize, String sortDir, String sortBy, String searchKey);
	Integer deleteUser(Integer uid, Integer updatedBy);
	Integer updateUser(DeleteUpdateUserRequest updateUser);
}
