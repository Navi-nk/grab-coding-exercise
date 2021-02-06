package com.navi.grabcodingexercise.entity;

import javax.persistence.*;

@Entity
@Table(name = "job")
public class Job extends BaseAuditEntity {

    @Column(name = "job_id", nullable = false)
    private String jobId;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "job_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(name = "run_id", nullable = false)
    private Integer runId;

    @Column(name = "script_file")
    private String scriptFile;

    @Column(name = "jar_file")
    private String jarFile;

    @Column(name = "main_class")
    private String mainClass;

    @Column(name = "params", length = 1000)
    private String params;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

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
