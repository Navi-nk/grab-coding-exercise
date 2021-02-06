package com.navi.grabcodingexercise.entity;

import com.navi.grabcodingexercise.entity.convertor.JobResultConvertor;
import com.navi.grabcodingexercise.model.JobResult;

import javax.persistence.*;

@Entity
@Table(name = "job_instance")
public class JobInstance extends BaseAuditEntity{

    @Column(name = "job_id", nullable = false)
    private String jobId;

    @Column(name = "job_instance_id", nullable = false)
    private String jobInstanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobGroupInstance jobGroupInstance;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @Column(name = "job_result", nullable = false)
    @Convert(converter = JobResultConvertor.class)
    @Lob
    private JobResult jobResult;

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

    public JobGroupInstance getJobGroupInstance() {
        return jobGroupInstance;
    }

    public void setJobGroupInstance(JobGroupInstance jobGroupInstance) {
        this.jobGroupInstance = jobGroupInstance;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public JobResult getJobResult() {
        return jobResult;
    }

    public void setJobResult(JobResult jobResult) {
        this.jobResult = jobResult;
    }
}
