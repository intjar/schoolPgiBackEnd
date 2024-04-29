package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.ToString;

@Data
@Validated
@ToString
public class RoleLinkMappingUpdateRequest {

  @NotEmpty
  private String jsonUrl;
  
  private String linkIds;
  
  @NotNull
  private Integer roleid;
  
  @NotNull
  private Integer loggedInUserId;
  
  
public String getJsonUrl() {
	return jsonUrl;
}
public void setJsonUrl(String jsonUrl) {
	this.jsonUrl = jsonUrl;
}
public String getLinkIds() {
	return linkIds;
}
public void setLinkIds(String linkIds) {
	this.linkIds = linkIds;
}
public Integer getRoleid() {
	return roleid;
}
public void setRoleid(Integer roleid) {
	this.roleid = roleid;
}
  
	
}
