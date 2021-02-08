package com.navi.grabcodingexercise.service.impl;

import com.navi.grabcodingexercise.entity.Group;
import com.navi.grabcodingexercise.entity.builder.JobGroupBuilder;
import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.repository.GroupRepository;
import com.navi.grabcodingexercise.service.JobGroupService;
import com.navi.grabcodingexercise.util.JsonConvertor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/***
 * Service implementation that has main logic for storing of JobGroup
 */
@Service
public class JobGroupServiceImpl implements JobGroupService {
    private static final Logger logger = LoggerFactory.getLogger(JobGroupServiceImpl.class);

    private final GroupRepository groupRepository;

    public JobGroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /***
     * Creates a Group in the system
     * @param request An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest}
     * @return An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest}
     */
    @Override
    public JobGroupRequest createJobGroup(JobGroupRequest request){
        logger.info(String.format("Received create request: %s", JsonConvertor.toJsonString(request)));
        try {
            JobGroupBuilder builder = new JobGroupBuilder(request);
            Group groupTobeCreated = builder.build();
            Group createdGroup = groupRepository.save(groupTobeCreated);
            return JobGroupRequest.from(createdGroup);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException || e instanceof NullPointerException || e.getCause() instanceof ConstraintViolationException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage()
                );
            throw e;
        }

    }

    /***
     * Used to update an existing Group
     * @param id Unique db identifier for the group
     * @param request An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest} with updated information to be saved
     * @return An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest}
     */
    @Override
    public JobGroupRequest saveJobGroup(Long id , JobGroupRequest request){
        logger.info(String.format("Received save request: %s for id: %d", JsonConvertor.toJsonString(request), id));
        try {
            Group groupTobeSaved = mayBeFindGroup(id);
            JobGroupBuilder builder = new JobGroupBuilder(groupTobeSaved, request);
            groupTobeSaved = builder.build();
            Group savedGroup = groupRepository.save(groupTobeSaved);
            return JobGroupRequest.from(savedGroup);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException || e instanceof NullPointerException || e.getCause() instanceof ConstraintViolationException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage()
                );
            throw e;
        }
    }

    /***
     * Fetches a JobGroup from given id
     * @param id Unique db identifier for the group
     * @return An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest}
     */
    @Override
    public JobGroupRequest getJobGroup(Long id){
        logger.info(String.format("Received get for id: %d", id));
        return JobGroupRequest.from(
                mayBeFindGroup(id)
        );
    }

    /***
     * Fetches a JobGroup from given id
     * @param groupId Unique user provided identifier for the group
     * @return An instance of type {@link com.navi.grabcodingexercise.model.JobGroupRequest}
     */
    @Override
    public JobGroupRequest getJobGroup(String groupId){
        logger.info(String.format("Received get for id: %s", groupId));
        return JobGroupRequest.from(
                mayBeFindGroup(groupId)
        );
    }

    /***
     * Deletes a JobGroup for a given id
     * @param id Unique db identifier for the group
     */
    @Override
    public void deleteJobGroup(Long id){
        logger.info(String.format("Received delete for id: %d", id));
        groupRepository.delete(
                mayBeFindGroup(id)
        );
    }

    private Group mayBeFindGroup(Long id){
        Optional<Group> jobGroup = groupRepository.findById(id);
        return jobGroup.orElseThrow( () ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format("Group not found for id: %d",id)
                )
        );
    }

    private Group mayBeFindGroup(String groupId){
        Optional<Group> jobGroup = groupRepository.findByGroupId(groupId);
        return jobGroup.orElseThrow( () ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format("Group not found for id: %s",groupId)
                )
        );
    }
}
