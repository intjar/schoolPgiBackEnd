package com.np.schoolpgi.dto.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.np.schoolpgi.model.LevelMaster;
import com.np.schoolpgi.model.Roles;

import lombok.Data;

@Data
public class SurveyListResponse {
	
    private Integer id;
    
  

    private String yearCode;
    

    private String surveyName;
    
    
    private String surveyDescription;
    
  
    private Date surveyStartDate;
    
 
    private Date surveyEndDate;
    
  
    private LevelMaster deoLevelId;
    
    private Roles deoRoleId;
    
    

    

    private LevelMaster approverLevelId;
    
    private Roles approverRoleId;
    
    
    private List<LevelMaster> viewerLevelId;
    
    private Map<String,List<Roles>> viewerRole;
    
    private List<LevelMaster> reviewerLevelId;
    
    private Map<String,List<Roles>> reviewerRole;
    
    
    private Integer reviewMandatory;
    
    private Integer status;
    
    private Boolean assignedSurveyStatus;
    
    private Boolean isNotified;
    
    private Boolean isNotifiedTpd;
    
    private Boolean isMappedQuest;
    
    private Boolean isAssignEnable;
    
    private String procedureName;

}
