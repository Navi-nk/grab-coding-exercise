package com.navi.grabcodingexercise.entity;

import com.navi.grabcodingexercise.entity.convertor.JobGroupAttributeConvertor;
import com.navi.grabcodingexercise.model.JobGroupRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/***
 * Entity class which is the backing ORM class for the table that stores Group execution info.
 */
@Entity
@Table(name = "job_group_instance",
        uniqueConstraints = @UniqueConstraint(name = "uc_group_instanceid", columnNames = {"group_instance_id"}))
public class JobGroupInstance extends BaseAuditEntity {

    @Column(name = "group_id", nullable = false)
    private String groupId;

    @Column(name = "group_instance_id", nullable = false)
    private String groupInstanceId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @Column(name = "job_group_snapshot", nullable = false)
    @Convert(converter = JobGroupAttributeConvertor.class)
    @Lob
    private JobGroupRequest jobGroupSnapshot;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "jobGroupInstance")
    private Set<JobInstance> jobInstances = new HashSet<>();

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

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
