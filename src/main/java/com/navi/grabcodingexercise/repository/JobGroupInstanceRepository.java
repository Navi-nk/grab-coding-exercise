package com.navi.grabcodingexercise.repository;

import com.navi.grabcodingexercise.entity.ExecutionStatus;
import com.navi.grabcodingexercise.entity.JobGroupInstance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface JobGroupInstanceRepository extends CrudRepository<JobGroupInstance, Long> {

    @Query(value = "select x FROM #{#entityName} x " +
            " WHERE x.groupId = :groupId " +
            " and x.status = :status")
    List<JobGroupInstance> findByGroupIdAndStatus(String groupId, ExecutionStatus status);

    Optional<JobGroupInstance> findByGroupInstanceId(String groupInstanceId);
}
