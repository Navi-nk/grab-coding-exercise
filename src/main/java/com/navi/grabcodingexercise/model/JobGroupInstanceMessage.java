package com.navi.grabcodingexercise.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.navi.grabcodingexercise.entity.ExecutionStatus;
import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.entity.JobInstance;
import com.navi.grabcodingexercise.entity.convertor.JobResultConvertor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

public class JobGroupInstanceMessage {

    private String groupId;
    String groupInstanceId;
    ExecutionStatus status;
    Set<JobInstance> jobInstances = new HashSet<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JobInstanceMessage {

        String jobId;
        String jobInstanceId;
        ExecutionStatus status;
        JobResult jobResult;

    }
}
