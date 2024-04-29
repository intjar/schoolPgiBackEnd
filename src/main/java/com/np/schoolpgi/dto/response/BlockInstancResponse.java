package com.np.schoolpgi.dto.response;

import java.util.List;

import com.np.schoolpgi.model.InstanceMaster;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BlockInstancResponse {
	private Boolean success;
	private Object appServiceCode;
	private List<InstanceMaster> result;
}
