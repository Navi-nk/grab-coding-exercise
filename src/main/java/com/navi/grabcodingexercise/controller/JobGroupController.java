package com.navi.grabcodingexercise.controller;

import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.service.JobGroupService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/jobstore")
public class JobGroupController {

    private final JobGroupService jobGroupService;

    public JobGroupController(JobGroupService jobGroupService) {
        this.jobGroupService = jobGroupService;
    }

    @PostMapping(value = "/save")
    public JobGroupRequest createJobGroup(@RequestBody JobGroupRequest request) throws Exception {
        return jobGroupService.createJobGroup(request);
    }

    @PutMapping(value = "/save/{id}")
    public JobGroupRequest saveJobGroup(@PathVariable Long id , @RequestBody JobGroupRequest request) throws Exception {
        return jobGroupService.saveJobGroup(id, request);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public JobGroupRequest fetchJobGroup(@PathVariable Long id){
        return jobGroupService.getJobGroup(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteJobGroup(@PathVariable Long id){
        jobGroupService.deleteJobGroup(id);
    }
}
