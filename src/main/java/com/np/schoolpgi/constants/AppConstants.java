package com.np.schoolpgi.constants;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class AppConstants {

	public static final long SIGNATURE_TIME_VALIDITY = 1000 * 60 * 5;
	
	public static final Direction QUERY_SORT_DIRECTION_DESC = Sort.Direction.DESC;
	public static final Direction QUERY_SORT_DIRECTION_ASC = Sort.Direction.ASC;
	public static final String QUERY_SORT_BY_COLUMN_NAME_ID = "id";
	public static final String sortDirectionDESC = "DESC";
	
//	public static final Integer NATIONAL_LEVEL_ID = 1;
//	public static final Integer STATE_LEVEL_ID = 2;
//	public static final Integer COSNTITUENCY_LEVEL_ID = 3;
//	public static final Integer DISTRICT_LEVEL_ID = 4;
//	public static final Integer BLOCK_LEVEL_ID = 5;
	public static final Integer SCHOOL_LEVEL_ID = 6;
	public static final String SCHOOL_LEVEL_NAME = "School";
	
	//Pagination
	
	public static final String DEFAULT_PAGE_NUMBER = "0";
    public  static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "created_at";
    public static final String DEFAULT_SORT_DIRECTION = "desc";
	public static final String DEFAULT_SEARCH_KEY = "";
	

}
