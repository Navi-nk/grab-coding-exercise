package com.navi.grabcodingexercise.controller;

import com.navi.grabcodingexercise.model.JobGroupInstanceMessage;
import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.model.JobResult;
import com.navi.grabcodingexercise.repository.GroupRepository;
import com.navi.grabcodingexercise.repository.JobGroupInstanceRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.navi.grabcodingexercise.util.JsonConvertor.toObject;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobGroupInstanceControllerTests {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private JobGroupInstanceRepository jobGroupInstanceRepository;

    private APIUtils apiUtils = new APIUtils();

    @Before
    public void setup(){
        mockMvc = webAppContextSetup(webApplicationContext).build();
        apiUtils.setMockMvc(mockMvc);
    }

    @After
    public void tearDown() {
        groupRepository.deleteAll();
        jobGroupInstanceRepository.deleteAll();
    }
    @Test
    public void should_create_group_execution() throws Exception {
        apiUtils.performJobStorePost(createGroupRequest());
        MockHttpServletResponse response = apiUtils.performJobInstancePost("test_group");
        assertEquals(200, response.getStatus());
        JobGroupInstanceMessage actual = toObject(response.getContentAsString(), JobGroupInstanceMessage.class);
        assertEquals("test_group", actual.getGroupId());
        assertEquals("running", actual.getStatus().getStatusValue());
    }

    @Test
    public void should_get_group_execution_instance() throws Exception {
        apiUtils.performJobStorePost(createGroupRequest());
        String executeResponse = apiUtils.performJobInstancePost("test_group").getContentAsString();
        String groupInstanceId = toObject(executeResponse, JobGroupInstanceMessage.class).getGroupInstanceId();

        TimeUnit.SECONDS.sleep(1);

        MockHttpServletResponse response = apiUtils.performJobInstanceGet(groupInstanceId);
        assertEquals(200, response.getStatus());
        JobGroupInstanceMessage actual = toObject(response.getContentAsString(), JobGroupInstanceMessage.class);
        JobResult[] actualResult = actual.getJobInstances().stream()
                .map(JobGroupInstanceMessage.JobInstanceMessage::getJobResult)
                .toArray(JobResult[]::new);
        JobResult[] expectedResult = createExpectedResult();
        assertEquals("test_group", actual.getGroupId());
        assertEquals("completed", actual.getStatus().getStatusValue());
        Assert.assertArrayEquals(actualResult, expectedResult);
    }

    @Test
    public void should_throw_exception_for_missing_group_get() throws Exception {
        MockHttpServletResponse response = apiUtils.performJobInstanceGet("missing_group");
        assertEquals(404, response.getStatus());
        assertEquals("Job Group instance not found for id: missing_group", response.getErrorMessage());
    }

    @Test
    public void should_throw_exception_for_missing_group() throws Exception {
        MockHttpServletResponse response = apiUtils.performJobInstancePost("missing_id");
        assertEquals(404, response.getStatus());
        assertEquals("Group not found for id: missing_id", response.getErrorMessage());
    }

    @Test
    public void should_get_cancel_execution_instance() throws Exception {
        apiUtils.performJobStorePost(createGroupRequest());
        String executeResponse = apiUtils.performJobInstancePost("test_group").getContentAsString();
        String groupInstanceId = toObject(executeResponse, JobGroupInstanceMessage.class).getGroupInstanceId();

        MockHttpServletResponse response = apiUtils.performJobInstanceCancel(groupInstanceId);
        assertEquals(200, response.getStatus());

    }

    private JobGroupRequest createGroupRequest() {
        InputStream is = this.getClass().getResourceAsStream("/test_execution_request.json");
        String request = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
        JobGroupRequest r = toObject(request, JobGroupRequest.class);
        String path = JobGroupInstanceControllerTests.class.getResource("/test.sh").getPath();
        r.getJobs().forEach(f -> f.setScriptPath(path));
        return r;
    }

    private JobResult[] createExpectedResult() {
        JobResult r1 = new JobResult();
        r1.setSuccess(true);
        r1.setStandardOutMessage("this is test message with args 1");

        return new JobResult[] {r1};
    }

}
