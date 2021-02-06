package com.navi.grabcodingexercise.entity;

import javax.persistence.*;

@Entity
@Table(name = "job")
public class Job extends BaseAuditEntity {

    @Column(name = "job_id", nullable = false)
    String jobId;

    @Column(name = "job_description")
    String jobDescription;

    @Column(name = "job_type", nullable = false)
    @Enumerated(EnumType.STRING)
    JobType jobType;

    @Column(name = "run_id", nullable = false)
    Integer runId;

    @Column(name = "script_file")
    String scriptFile;

    @Column(name = "jar_file")
    String jarFile;

    @Column(name = "main_class")
    String mainClass;

    @Column(name = "params", length = 1000)
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
