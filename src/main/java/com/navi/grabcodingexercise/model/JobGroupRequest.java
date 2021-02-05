package com.navi.grabcodingexercise.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.navi.grabcodingexercise.entity.Group;
import com.navi.grabcodingexercise.entity.Job;

import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobGroupRequest {

    Long id;
    String groupId;
    String groupDescription;
    Set<JobRequest> jobs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public Set<JobRequest> getJobs() {
        return jobs;
    }

    public void setJobs(Set<JobRequest> jobs) {
        this.jobs = jobs;
    }

    public static JobGroupRequest from(Group source){
        JobGroupRequest request = new JobGroupRequest();
        request.setId(source.getId());
        request.setGroupId(source.getGroupId());
        request.setGroupDescription(source.getGroupDescription());
        request.setJobs(JobRequest.from(source.getJobs()));
        return request;
    }

    public static class JobRequest {
        String jobId;
        String jobDescription;
        String jobType;
        Integer runId;
        String scriptPath;
        String jarPath;
        String mainClass;
        String params;

        public static Set<JobRequest> from(Set<Job> jobs) {
            return jobs.stream().map( j -> {
                JobRequest jobRequest = new JobRequest();
                jobRequest.setJobId(j.getJobId());
                jobRequest.setJobDescription(j.getJobDescription());
                jobRequest.setRunId(j.getRunId());
                jobRequest.setJobType(j.getJobType().getJobTypeValue());
                jobRequest.setJarPath(j.getJarFile());
                jobRequest.setScriptPath(j.getScriptFile());
                jobRequest.setMainClass(j.getMainClass());
                jobRequest.setParams(j.getParams());
                return jobRequest;
            }).collect(Collectors.toSet());
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getJobDescription() {
            return jobDescription;
        }

        public void setJobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
        }

        public String getJobType() {
            return jobType;
        }

        public void setJobType(String jobType) {
            this.jobType = jobType;
        }

        public Integer getRunId() {
            return runId;
        }

        public void setRunId(Integer runId) {
            this.runId = runId;
        }

        public String getScriptPath() {
            return scriptPath;
        }

        public void setScriptPath(String scriptPath) {
            this.scriptPath = scriptPath;
        }

        public String getJarPath() {
            return jarPath;
        }

        public void setJarPath(String jarPath) {
            this.jarPath = jarPath;
        }

        public String getMainClass() {
            return mainClass;
        }

        public void setMainClass(String mainClass) {
            this.mainClass = mainClass;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
    }
}
