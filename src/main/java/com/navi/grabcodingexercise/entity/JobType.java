package com.navi.grabcodingexercise.entity;

/***
 * Enum representing different type of processes that can be executed in the system.
 */
public enum JobType {
    JAVA_PROCESS("java"),
    PYTHON_SCRIPT("python"),
    SHELL_SCRIPT("shell");

    private final String value;

    JobType(String value){
        this.value = value;
    }

    public String getJobTypeValue() {
        return value;
    }

    public static JobType findJobType(String value){
        for(JobType j : values()){
            if(j.value.equalsIgnoreCase(value)){
                return j;
            }
        }
        return null;
    }
}
