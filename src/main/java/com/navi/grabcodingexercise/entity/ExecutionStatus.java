package com.navi.grabcodingexercise.entity;

/***
 * Enum representing different states of Job Group execution
 */
public enum ExecutionStatus {
    RUNNING("running"),
    COMPLETED("completed"),
    FAILED("failed");

    private final String value;

    ExecutionStatus(String value){
        this.value = value;
    }

    public String getStatusValue() {
        return value;
    }

    public static ExecutionStatus findStatus(String value){
        for(ExecutionStatus j : values()){
            if(j.value.equalsIgnoreCase(value)){
                return j;
            }
        }
        return null;
    }
}
