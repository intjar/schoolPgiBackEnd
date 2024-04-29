package com.np.schoolpgi.dto.response;

import java.util.List;

import com.np.schoolpgi.dto.request.LevelIdNameModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DashboardLevelIdResponse {

	private List<LevelIdNameModel> levelList;
}
