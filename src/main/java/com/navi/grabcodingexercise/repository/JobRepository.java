package com.navi.grabcodingexercise.repository;

import com.navi.grabcodingexercise.entity.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Long> {
}
