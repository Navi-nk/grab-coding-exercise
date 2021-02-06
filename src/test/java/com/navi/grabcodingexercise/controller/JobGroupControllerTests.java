package com.navi.grabcodingexercise.controller;

import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.repository.GroupRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.navi.grabcodingexercise.model.JobGroupRequest.JobRequest;
import static com.navi.grabcodingexercise.util.JsonConvertor.toJsonString;
import static com.navi.grabcodingexercise.util.JsonConvertor.toObject;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobGroupControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GroupRepository groupRepository;

    @Before
    public void setup(){
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() {
        groupRepository.deleteAll();
    }

    @Test
    public void should_create_new_job_group() throws Exception {
        JobGroupRequest request = createGroupRequest();
        MockHttpServletResponse response = performPost(request);

        assertEquals(200, response.getStatus());
        JobGroupRequest actual = toObject(response.getContentAsString(), JobGroupRequest.class);
        request.setId(actual.getId());
        assertEquals(request, actual);
    }

    @Test
    public void should_update_existing_job_group() throws Exception {
        JobGroupRequest postResponse = createInitialGroup(createGroupRequest());

        postResponse.setGroupId("test_group2");
        JobRequest jobRequest = postResponse.getJobs().stream().filter( j -> j.getJobId().equals("job3")).findAny().get();
        postResponse.getJobs().remove(jobRequest);

        MockHttpServletResponse response = performPut(postResponse, postResponse.getId());
        JobGroupRequest actual = toObject(response.getContentAsString(), JobGroupRequest.class);

        assertEquals(200, response.getStatus());
        assertEquals(postResponse, actual);
    }

    @Test
    public void should_get_created_job_group() throws Exception {
        JobGroupRequest postResponse = createInitialGroup(createGroupRequest());

        MockHttpServletResponse response = performGet(postResponse.getId());
        JobGroupRequest actual = toObject(response.getContentAsString(), JobGroupRequest.class);

        assertEquals(200, response.getStatus());
        assertEquals(postResponse, actual);
    }

    @Test
    public void should_delete_created_job_group() throws Exception {
        JobGroupRequest postResponse = createInitialGroup(createGroupRequest());

        MockHttpServletResponse response = performDelete(postResponse.getId());
        assertEquals(200, response.getStatus());

        int getStatus = performGet(postResponse.getId()).getStatus();
        assertEquals(404, getStatus);
    }

    @Test
    public void should_return_404_for_missing_group_get() throws Exception {
        int getStatus = performGet(1L).getStatus();
        assertEquals(404, getStatus);
    }

    @Test
    public void should_return_404_for_missing_group_delete() throws Exception {
        int getStatus = performGet(1L).getStatus();
        assertEquals(404, getStatus);
    }

    @Test
    public void should_throw_exception_for_missing_group_id() throws Exception {
        JobGroupRequest request = createInvalidGroupRequest(null, null, null);
        request.setGroupId(null);

        MockHttpServletResponse response = performPost(request);

        assertEquals(400, response.getStatus());
        assertEquals("Group Id cannot be Null", response.getErrorMessage());

    }

    @Test
    public void should_throw_exception_for_same_group_id() throws Exception {
        JobGroupRequest postResponse = createInitialGroup(createGroupRequest());

        MockHttpServletResponse response = performPost(postResponse);

        assertEquals(400, response.getStatus());
        assertThat(response.getErrorMessage(), containsString("ConstraintViolationException"));
    }

    @Test
    public void should_throw_exception_for_missing_jobs() throws Exception {
        JobGroupRequest request = createInvalidGroupRequest(null, null, null);
        request.setJobs(new HashSet<>());

        MockHttpServletResponse response = performPost(request);
        assertEquals(400, response.getStatus());
        assertEquals("Jobs request cannot be empty", response.getErrorMessage());

    }

    @Test
    public void should_throw_exception_for_missing_jobId() throws Exception {
        JobGroupRequest request = createInvalidGroupRequest(null, null, null);

        MockHttpServletResponse response = performPost(request);
        assertEquals(400, response.getStatus());
        assertEquals("Job Id cannot be null", response.getErrorMessage());
    }

    @Test
    public void should_throw_exception_for_missing_runId() throws Exception {
        JobGroupRequest request = createInvalidGroupRequest("someid", null, null);

        MockHttpServletResponse response = performPost(request);
        assertEquals(400, response.getStatus());
        assertEquals("run id cannot be null", response.getErrorMessage());
    }

    @Test
    public void should_throw_exception_for_missing_JobType() throws Exception {
        JobGroupRequest request = createInvalidGroupRequest("someid", 1, null);

        MockHttpServletResponse response = performPost(request);
        assertEquals(400, response.getStatus());
        assertEquals("Job Type cannot be null", response.getErrorMessage());
    }

    @Test
    public void should_throw_exception_for_invalid_JobType() throws Exception {
        JobGroupRequest request = createInvalidGroupRequest("someid", 1, "javascript");

        MockHttpServletResponse response = performPost(request);
        assertEquals(400, response.getStatus());
        assertEquals("JobType not found for:javascript", response.getErrorMessage());
    }

    @Test
    public void should_throw_exception_for_invalid_runId() throws Exception {
        JobGroupRequest request = createGroupRequest();
        JobRequest jobRequest = request.getJobs().stream().filter( j -> j.getJobId().equals("job2")).findAny().get();
        jobRequest.setRunId(1);

        MockHttpServletResponse response = performPost(request);
        assertEquals(400, response.getStatus());
        assertEquals("Jobs must have unique run ids", response.getErrorMessage());
    }

    @Test
    public void should_throw_exception_for_invalid_java_args() throws Exception {
        JobGroupRequest request = createInvalidGroupRequest("someid", 1, "java");

        MockHttpServletResponse response = performPost(request);
        assertEquals(400, response.getStatus());
        assertEquals("Java process args jarPath & mainClass are needed", response.getErrorMessage());
    }

    @Test
    public void should_throw_exception_for_invalid_script_args() throws Exception {
        JobGroupRequest request = createInvalidGroupRequest("someid", 1, "python");

        MockHttpServletResponse response = performPost(request);
        assertEquals(400, response.getStatus());
        assertEquals("Script path is needed for provided JobType", response.getErrorMessage());
    }

    private JobGroupRequest createGroupRequest() {
        InputStream is = this.getClass().getResourceAsStream("/test_request.json");
        String request = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
        return toObject(request, JobGroupRequest.class);
    }

    private JobGroupRequest createInvalidGroupRequest(String jobId, Integer runId, String jobType) {
        JobGroupRequest request = new JobGroupRequest();
        request.setGroupId("test_group");
        JobRequest job = new JobRequest();
        job.setJobId(jobId);
        job.setRunId(runId);
        job.setJobType(jobType);

        request.setJobs(new HashSet<>(Collections.singletonList(job)));
        return request;
    }

    private MockHttpServletResponse performPost(JobGroupRequest request) throws Exception {
        return mockMvc.perform(post("/jobstore/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)))
                .andReturn()
                .getResponse();
    }

    private JobGroupRequest createInitialGroup(JobGroupRequest request) throws Exception {
        String initialGroup = performPost(request)
                .getContentAsString();
        return toObject(initialGroup, JobGroupRequest.class);
    }

    private MockHttpServletResponse performPut(JobGroupRequest request, Long id) throws Exception {
        return mockMvc.perform(put("/jobstore/save/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)))
                .andReturn()
                .getResponse();
    }

    private MockHttpServletResponse performGet(Long id) throws Exception {
        return mockMvc.perform(get("/jobstore/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }

    private MockHttpServletResponse performDelete(Long id) throws Exception {
        return mockMvc.perform(delete("/jobstore/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }
}
