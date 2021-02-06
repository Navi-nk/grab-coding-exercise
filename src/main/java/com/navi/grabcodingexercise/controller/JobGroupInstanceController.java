package com.navi.grabcodingexercise.controller;

import com.navi.grabcodingexercise.jobexecutor.JobGroupInstanceExecutor;
import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.repository.JobGroupInstanceRepository;
import com.navi.grabcodingexercise.service.JobGroupInstanceService;
import com.navi.grabcodingexercise.service.JobGroupService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping(value = "/jobGroup")
public class JobGroupInstanceController {

    private final JobGroupInstanceRepository jobGroupInstanceRepository;
    private final JobGroupInstanceService jobGroupInstanceService;
    private final JobGroupService jobGroupService;
    private final Environment environment;
    private ExecutorService executorService;

    public JobGroupInstanceController(JobGroupInstanceRepository jobGroupInstanceRepository, JobGroupInstanceService jobGroupInstanceService, JobGroupService jobGroupService, Environment environment) {
        this.jobGroupInstanceRepository = jobGroupInstanceRepository;
        this.jobGroupInstanceService = jobGroupInstanceService;
        this.jobGroupService = jobGroupService;
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        int executorCount = environment.getProperty("job.executor.threads", Integer.class, 3);
        executorService = Executors.newFixedThreadPool(executorCount);
    }

//    @PostMapping
//    public

    @GetMapping(value = "/instance/{id}")
    @ResponseBody
    public void someFunc(@PathVariable Long id) {
        JobGroupRequest request = jobGroupService.getJobGroup(id);
        new JobGroupInstanceExecutor(jobGroupInstanceRepository, executorService).execute(request);
    }
}
