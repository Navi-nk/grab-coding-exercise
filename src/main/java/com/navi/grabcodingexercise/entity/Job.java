package com.navi.grabcodingexercise.entity;

import javax.persistence.*;

@Entity
@Table(name = "job")
public class Job extends BaseAuditEntity{
    /*"job_id": "",
            "job_description": "",
            "job_type": "",
            "run_id": 1,
            "scriptFile": "",
            "jarFile": "",
            "main_class": "",
            "params": ""

     */
    @Column
    String jobId;

    @Column
    String jobDescription;

    @Column
    @Enumerated(EnumType.STRING)
    JobType jobType;

    @Column
    Integer runId;

    @Column
    String scriptFile;

    @Column
    String jarFile;

    @Column(length = 1000)
    String params;

    @ManyToOne(fetch = FetchType.LAZY)
    Group group;

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

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public Integer getRunId() {
        return runId;
    }

    public void setRunId(Integer runId) {
        this.runId = runId;
    }

    public String getScriptFile() {
        return scriptFile;
    }

    public void setScriptFile(String scriptFile) {
        this.scriptFile = scriptFile;
    }

    public String getJarFile() {
        return jarFile;
    }

    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
