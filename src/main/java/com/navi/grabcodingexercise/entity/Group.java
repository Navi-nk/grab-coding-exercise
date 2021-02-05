package com.navi.grabcodingexercise.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job_group",
        uniqueConstraints = @UniqueConstraint(name = "uc_groupid", columnNames = {"group_id"}))
public class Group extends BaseAuditEntity{

    @Column(name = "group_id")
    String groupId;

    @Column(name = "group_description", length = 1000)
    String groupDescription;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "group")
    Set<Job> jobs = new HashSet<>();

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

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs.clear();
        this.jobs.addAll(jobs);
    }
}
