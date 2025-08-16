package com.plugin.utils;

import com.plugin.common.Constants;
import com.plugin.controller.TestController;
import com.plugin.utils.SpringUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

@SpringBootTest(classes = com.plugin.PluginLoaderApplication.class)
@AutoConfigureMockMvc
public class SpringUtilsTests {

    @Resource
    private RequestMappingHandlerMapping mapping;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testHandleControllerRegistration() throws Exception {
        SpringUtils.handleControllerRegistration(
                new TestController(),
                mapping,
                "/test",
                Constants.REGISTERER);

        // 测试注册的Controller是否能正常工作
        mockMvc.perform(post("/test/api/post"))
                .andExpect(status().isOk())
                .andExpect(content().string("postMapping"))
                .andExpect(handler().handlerType(TestController.class))
                .andExpect(handler().methodName("post"));

        mockMvc.perform(get("/test/api/get"))
                .andExpect(status().isOk())
                .andExpect(content().string("getMapping"))
                .andExpect(handler().handlerType(TestController.class))
                .andExpect(handler().methodName("get"));

        SpringUtils.handleControllerRegistration(
                new TestController(),
                mapping,
                "/test",
                Constants.UN_REGISTERER);

        // 测试卸载后的Controller是否能正常工作
        mockMvc.perform(get("/test/api/post"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/test/api/get"))
                .andExpect(status().isNotFound());
    }
}
