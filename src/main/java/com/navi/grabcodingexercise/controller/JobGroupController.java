package com.navi.grabcodingexercise.controller;

import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.service.JobGroupService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for exposing APIs for Job Group management
 */
@RestController
@RequestMapping(value = "/jobstore")
public class JobGroupController {

    private final JobGroupService jobGroupService;

    public JobGroupController(JobGroupService jobGroupService) {
        this.jobGroupService = jobGroupService;
    }

    /***
     *  API to save Job Group request. This is will register a Group in the system which can be executed later.
     * @param request An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest}
     * @return An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest}. This object is created from new entry made and contains id information
     * @throws Exception
     */
    @PostMapping(value = "/save")
    public JobGroupRequest createJobGroup(@RequestBody JobGroupRequest request) throws Exception {
        return jobGroupService.createJobGroup(request);
    }

    /***
     * API to update an existing Job Group.
     * @param id Unique db identifier for the group
     * @param request An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest} with updated information to be saved
     * @return An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest}
     * @throws Exception
     */
    @PutMapping(value = "/save/{id}")
    public JobGroupRequest saveJobGroup(@PathVariable Long id , @RequestBody JobGroupRequest request) throws Exception {
        return jobGroupService.saveJobGroup(id, request);
    }

    /***
     * API to fetch a JobGroup
     * @param id Unique db identifier for the group
     * @return An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest}
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JobGroupRequest fetchJobGroup(@PathVariable Long id){
        return jobGroupService.getJobGroup(id);
    }

    /***
     * API to fetch a JobGroup
     * @param groupId Unique user provided identifier for the group
     * @return An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest}
     */
    @GetMapping(value = "/group")
    @ResponseBody
    public JobGroupRequest fetchJobGroupByGroupId(@RequestParam("groupId") String groupId){
        return jobGroupService.getJobGroup(groupId);
    }

    /**
     * API to delete a JobGroup
     * @param id Unique db identifier for the group
     */
    @DeleteMapping(value = "/{id}")
    public void deleteJobGroup(@PathVariable Long id){
        jobGroupService.deleteJobGroup(id);
    }
}
