package com.navi.grabcodingexercise.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/***
 * Model that represents the result of a given Job execution
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobResult {
    Boolean success;
    String standardOutMessage;
    String standardErrorMessage;
    String exceptionMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobResult)) return false;
        JobResult result = (JobResult) o;
        return Objects.equals(getSuccess(), result.getSuccess()) &&
                Objects.equals(getStandardOutMessage(), result.getStandardOutMessage()) &&
                Objects.equals(getStandardErrorMessage(), result.getStandardErrorMessage()) &&
                Objects.equals(getExceptionMessage(), result.getExceptionMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSuccess(), getStandardOutMessage(), getStandardErrorMessage(), getExceptionMessage());
    }

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
