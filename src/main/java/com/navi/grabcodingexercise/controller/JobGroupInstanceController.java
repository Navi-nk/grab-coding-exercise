package com.navi.grabcodingexercise.controller;

import com.navi.grabcodingexercise.model.JobGroupInstanceMessage;
import com.navi.grabcodingexercise.service.JobGroupInstanceService;
import org.springframework.web.bind.annotation.*;

/***
 * Controller for exposing APIs for execution and monitoring of Job Group
 */
@RestController
@RequestMapping(value = "/jobGroup")
public class JobGroupInstanceController {

    JobGroupInstanceService jobGroupInstanceService;

    public JobGroupInstanceController(JobGroupInstanceService jobGroupInstanceService) {
        this.jobGroupInstanceService = jobGroupInstanceService;
    }

    /***
     * API to trigger execution of the group
     * @param groupId Unique user provided identifier for the group
     * @return An instance of {@link com.navi.grabcodingexercise.model.JobGroupInstanceMessage}. This model contains status information on each of Jobs in the Group.
     */
    @PostMapping(value = "/execute/{groupId}")
    public JobGroupInstanceMessage executeJobGroup(@PathVariable("groupId") String groupId) {
        return jobGroupInstanceService.executeJobGroup(groupId);
    }

    /**
     * API to fetch execution status of the group
     * @param instanceId Unique System generated identifier for the group execution. This is returned in the response of the execute API.
     * @return An instance of {@link com.navi.grabcodingexercise.model.JobGroupInstanceMessage}
     */
    @GetMapping(value = "/execution")
    @ResponseBody
    public JobGroupInstanceMessage fetchJobInstance(@RequestParam("instanceId") String instanceId) {
        return jobGroupInstanceService.fetchJobGroupInstance(instanceId);
    }

    /***
     * API to cancel execution of the group
     * @param instanceId Unique System generated identifier for the group execution
     */
    @PostMapping(value = "/execution/cancel/{instanceId}")
    public void cancelJobGroupExecution(@PathVariable("instanceId") String instanceId) {
         jobGroupInstanceService.cancelJobGroupExecution(instanceId);
    }
}
