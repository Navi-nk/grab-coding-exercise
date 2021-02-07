package com.navi.grabcodingexercise.service;

import com.navi.grabcodingexercise.model.JobGroupInstanceMessage;

public interface JobGroupInstanceService {
    JobGroupInstanceMessage executeJobGroup(String groupId);
    JobGroupInstanceMessage fetchJobGroupInstance(String jobGroupInstanceId);
    void cancelJobGroupExecution(String jobGroupInstanceId);
}
