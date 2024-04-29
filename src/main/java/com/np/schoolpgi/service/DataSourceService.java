package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.request.DataSourceRequest;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.model.DataSource;

public interface DataSourceService {

	Integer createDataSource(DataSourceRequest dataRequest);
	List<DataSource> viewDataSource(String sortByColumn ,String sortDirection);
	Integer deleteDataSource(DeleteRequestDto dataRequest);
}
