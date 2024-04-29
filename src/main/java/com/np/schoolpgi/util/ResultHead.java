package com.np.schoolpgi.util;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultHead {
    private long surveyID;
    private String yearCode;
    private String surveyName;
    private String surveyDescription;
    private long yearAssignedOn;
    private LocalDate startDate;
    private String activeUpto;
    private String eventName;
    private long loginId;
    private Integer instanceId;

    @JsonProperty("survey_id")
    public long getSurveyID() { return surveyID; }
    @JsonProperty("survey_id")
    public void setSurveyID(long value) { this.surveyID = value; }

    @JsonProperty("year_code")
    public String getYearCode() { return yearCode; }
    @JsonProperty("year_code")
    public void setYearCode(String value) { this.yearCode = value; }

    @JsonProperty("survey_name")
    public String getSurveyName() { return surveyName; }
    @JsonProperty("survey_name")
    public void setSurveyName(String value) { this.surveyName = value; }

    @JsonProperty("survey_description")
    public String getSurveyDescription() { return surveyDescription; }
    @JsonProperty("survey_description")
    public void setSurveyDescription(String value) { this.surveyDescription = value; }

    @JsonProperty("year_assigned_on")
    public long getYearAssignedOn() { return yearAssignedOn; }
    @JsonProperty("year_assigned_on")
    public void setYearAssignedOn(long value) { this.yearAssignedOn = value; }

    @JsonProperty("start_date")
    public LocalDate getStartDate() { return startDate; }
    @JsonProperty("start_date")
    public void setStartDate(LocalDate value) { this.startDate = value; }

    @JsonProperty("active_upto")
    public String getActiveUpto() { return activeUpto; }
    @JsonProperty("active_upto")
    public void setActiveUpto(String value) { this.activeUpto = value; }
    @JsonProperty("event_name")
    public String getEventName() { return eventName; }
    @JsonProperty("event_name")
    public void setEventName(String value) { this.eventName = value; }
    @JsonProperty("login_id")
    public long getLoginId() { return loginId; }
    @JsonProperty("login_id")
    public void setLoginId(long value) { this.loginId = value; }
    
    @JsonProperty("inst_id")
    public long getInstanceId() { return instanceId; }
    @JsonProperty("inst_id")
    public void setInatanceId(Integer value) { this.instanceId = value; }
}
