package com.navi.grabcodingexercise.service;

import com.navi.grabcodingexercise.model.JobGroupRequest;

public interface JobGroupService {
    JobGroupRequest createJobGroup(JobGroupRequest request);
    JobGroupRequest saveJobGroup(Long id , JobGroupRequest request);
    JobGroupRequest getJobGroup(Long id);
    JobGroupRequest getJobGroup(String groupId);
    void deleteJobGroup(Long id);
}
