package com.np.schoolpgi.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestSaveSurveyData {
    private ResultHead[] resultHead;
    private ResultDetRequest[] resultDet;

    @JsonProperty("result_head")
    public ResultHead[] getResultHead() { return resultHead; }
    @JsonProperty("result_head")
    public void setResultHead(ResultHead[] value) { this.resultHead = value; }

    @JsonProperty("result_det")
    public ResultDetRequest[] getResultDet() { return resultDet; }
    @JsonProperty("result_det")
    public void setResultDet(ResultDetRequest[] value) { this.resultDet = value; }
}
