package com.navi.grabcodingexercise.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.navi.grabcodingexercise.entity.ExecutionStatus;
import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.entity.JobInstance;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.navi.grabcodingexercise.model.JobGroupRequest.JobRequest;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobGroupInstanceMessage {

    private String groupId;
    private String groupInstanceId;
    private ExecutionStatus status;
    private Set<JobInstanceMessage> jobInstances = new HashSet<>();
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static JobGroupInstanceMessage from(JobGroupInstance jobGroupInstance) {
        JobGroupInstanceMessage message = new JobGroupInstanceMessage();
        message.setGroupId(jobGroupInstance.getGroupId());
        message.setGroupInstanceId(jobGroupInstance.getGroupInstanceId());
        message.setStatus(jobGroupInstance.getStatus());
        message.setStartTime(jobGroupInstance.getStartTime());
        message.setEndTime(jobGroupInstance.getEndTime());
        message.setJobInstances(JobInstanceMessage.from(jobGroupInstance.getJobInstances(), jobGroupInstance.getJobGroupSnapshot().getJobs()));
        return message;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupInstanceId() {
        return groupInstanceId;
    }

    public void setGroupInstanceId(String groupInstanceId) {
        this.groupInstanceId = groupInstanceId;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public Set<JobInstanceMessage> getJobInstances() {
        return jobInstances;
    }

    public void setJobInstances(Set<JobInstanceMessage> jobInstances) {
        this.jobInstances = jobInstances;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JobInstanceMessage {

        private String jobId;
        private String jobInstanceId;
        private JobRequest jobRequest;
        private JobResult jobResult;
        private ExecutionStatus status;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public static Set<JobInstanceMessage> from(Set<JobInstance> jobInstances, Set<JobRequest> jobRequests) {
            return jobInstances.stream().map( j -> {
                JobInstanceMessage message = new JobInstanceMessage();
                message.setJobId(j.getJobId());
                message.setJobInstanceId(j.getJobInstanceId());
                message.setJobResult(j.getJobResult());
                message.setStatus(j.getStatus());
                message.setJobRequest(jobRequestById(j.getJobId(), jobRequests));
                message.setStartTime(j.getStartTime());
                message.setEndTime(j.getEndTime());
                return message;
            }).collect(Collectors.toSet());
        }

        private static JobRequest jobRequestById(String jobId, Set<JobRequest> jobRequests){
            return jobRequests.stream()
                    .filter(j -> j.getJobId().equals(jobId))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Job request not found for "+ jobId));
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getJobInstanceId() {
            return jobInstanceId;
        }

        public void setJobInstanceId(String jobInstanceId) {
            this.jobInstanceId = jobInstanceId;
        }

        public JobRequest getJobRequest() {
            return jobRequest;
        }

        public void setJobRequest(JobRequest jobRequest) {
            this.jobRequest = jobRequest;
        }

        public JobResult getJobResult() {
            return jobResult;
        }

        public void setJobResult(JobResult jobResult) {
            this.jobResult = jobResult;
        }

        public ExecutionStatus getStatus() {
            return status;
        }

        public void setStatus(ExecutionStatus status) {
            this.status = status;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
        }
    }
}
