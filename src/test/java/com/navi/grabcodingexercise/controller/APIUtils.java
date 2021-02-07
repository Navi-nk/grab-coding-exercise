package com.navi.grabcodingexercise.controller;

import com.navi.grabcodingexercise.model.JobGroupRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static com.navi.grabcodingexercise.util.JsonConvertor.toJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class APIUtils {
    private MockMvc mockMvc;

    MockHttpServletResponse performJobStorePost(JobGroupRequest request) throws Exception {
        return mockMvc.perform(post("/jobstore/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)))
                .andReturn()
                .getResponse();
    }

     MockHttpServletResponse performJobStorePut(JobGroupRequest request, Long id) throws Exception {
        return mockMvc.perform(put("/jobstore/save/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)))
                .andReturn()
                .getResponse();
    }

     MockHttpServletResponse performJobStoreGet(Long id) throws Exception {
        return mockMvc.perform(get("/jobstore/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }

     MockHttpServletResponse performJobStoreDelete(Long id) throws Exception {
        return mockMvc.perform(delete("/jobstore/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }

    MockHttpServletResponse performJobInstancePost(String groupId) throws Exception {
        return mockMvc.perform(post("/jobGroup/execute/"+groupId)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }

    MockHttpServletResponse performJobInstanceGet(String groupId) throws Exception {
        return mockMvc.perform(get("/jobGroup/execution?instanceId="+groupId)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }

    MockHttpServletResponse performJobInstanceCancel(String groupId) throws Exception {
        return mockMvc.perform(post("/jobGroup/execution/cancel/"+groupId)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }


    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
}
