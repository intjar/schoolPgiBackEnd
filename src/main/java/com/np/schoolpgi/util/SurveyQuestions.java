package com.np.schoolpgi.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SurveyQuestions {
    private boolean status;
    private String message;
    private ResultHead[] resultHead;
    private ResultDet[] resultDet;

    @JsonProperty("status")
    public boolean getStatus() { return status; }
    @JsonProperty("status")
    public void setStatus(boolean value) { this.status = value; }

    @JsonProperty("message")
    public String getMessage() { return message; }
    @JsonProperty("message")
    public void setMessage(String value) { this.message = value; }

    @JsonProperty("result_head")
    public ResultHead[] getResultHead() { return resultHead; }
    @JsonProperty("result_head")
    public void setResultHead(ResultHead[] value) { this.resultHead = value; }

    @JsonProperty("result_det")
    public ResultDet[] getResultDet() { return resultDet; }
    @JsonProperty("result_det")
    public void setResultDet(ResultDet[] value) { this.resultDet = value; }
}
