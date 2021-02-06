package com.navi.grabcodingexercise.entity;

import com.navi.grabcodingexercise.entity.convertor.JobGroupAttributeConvertor;
import com.navi.grabcodingexercise.model.JobGroupRequest;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job_group_instance")
public class JobGroupInstance extends BaseAuditEntity {

    @Column(name = "group_id", nullable = false)
    String groupId;

    @Column(name = "group_instance_id", nullable = false)
    String groupInstanceId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    ExecutionStatus status;

    @Column(name = "job_group_snapshot", nullable = false)
    @Convert(converter = JobGroupAttributeConvertor.class)
    @Lob
    private JobGroupRequest jobGroupSnapshot;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "jobGroupInstance")
    Set<JobInstance> jobInstances = new HashSet<>();

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

    public JobGroupRequest getJobGroupSnapshot() {
        return jobGroupSnapshot;
    }

    public void setJobGroupSnapshot(JobGroupRequest jobGroupSnapshot) {
        this.jobGroupSnapshot = jobGroupSnapshot;
    }

    public Set<JobInstance> getJobInstances() {
        return jobInstances;
    }

    public void setJobInstances(Set<JobInstance> jobInstances) {
        this.jobInstances = jobInstances;
    }
}
