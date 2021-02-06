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

    @PostMapping(value = "/execute")
    public JobGroupInstanceMessage executeJobGroup(@RequestParam("groupId") String groupId) {
        return jobGroupInstanceService.executeJobGroup(groupId);
    }

    @GetMapping(value = "/execution")
    @ResponseBody
    public JobGroupInstanceMessage someFunc(@RequestParam("instanceId") String instanceId) {
        return null;
    }
}
