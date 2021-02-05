package com.navi.grabcodingexercise.entity;

public enum JobType {
    JAVA_PROCESS("java"),
    PYTHON_SCRIPT("python"),
    SHELL_SCRIPT("shell");

    private final String value;

    private JobType(String value){
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
        throw new IllegalArgumentException("JobType not found for:"+value);
    }
}
