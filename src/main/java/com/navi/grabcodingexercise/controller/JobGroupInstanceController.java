package com.navi.grabcodingexercise.controller;

import com.navi.grabcodingexercise.model.JobGroupInstanceMessage;
import com.navi.grabcodingexercise.service.JobGroupInstanceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/jobGroup")
public class JobGroupInstanceController {

    JobGroupInstanceService jobGroupInstanceService;

    public JobGroupInstanceController(JobGroupInstanceService jobGroupInstanceService) {
        this.jobGroupInstanceService = jobGroupInstanceService;
    }

    @PostMapping(value = "/execute/{groupId}")
    public JobGroupInstanceMessage executeJobGroup(@PathVariable("groupId") String groupId) {
        return jobGroupInstanceService.executeJobGroup(groupId);
    }

    @GetMapping(value = "/execution")
    @ResponseBody
    public JobGroupInstanceMessage fetchJobInstance(@RequestParam("instanceId") String instanceId) {
        return jobGroupInstanceService.fetchJobGroupInstance(instanceId);
    }

    @PostMapping(value = "/execution/cancel/{instanceId}")
    public void cancelJobGroupExecution(@PathVariable("instanceId") String instanceId) {
         jobGroupInstanceService.cancelJobGroupExecution(instanceId);
    }
}
