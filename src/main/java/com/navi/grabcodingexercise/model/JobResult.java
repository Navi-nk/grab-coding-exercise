package com.navi.grabcodingexercise.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobResult {
    Boolean success;
    String standardOutMessage;
    String standardErrorMessage;
    String exceptionMessage;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getStandardOutMessage() {
        return standardOutMessage;
    }

    public void setStandardOutMessage(String standardOutMessage) {
        this.standardOutMessage = standardOutMessage;
    }

    public String getStandardErrorMessage() {
        return standardErrorMessage;
    }

    public void setStandardErrorMessage(String standardErrorMessage) {
        this.standardErrorMessage = standardErrorMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
