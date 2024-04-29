package com.np.schoolpgi.model;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.Data;

@Data
public class SurveyMasterCompositeKey implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Column(name="year_code", nullable = false)
    private String yearCode;
    @Column(name="survey_name", nullable = false)
    private String surveyName;
}