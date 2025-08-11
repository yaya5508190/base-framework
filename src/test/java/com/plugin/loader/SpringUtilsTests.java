package com.plugin.loader;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

@SpringBootTest(classes = com.yx.plugin.loader.PluginLoaderApplication.class)
@AutoConfigureMockMvc
public class SpringUtilsTests {

    @Resource
    private RequestMappingHandlerMapping mapping;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testRegisterController() throws Exception {
        SpringUtils.registerController(new TestController(), mapping, "/test");

        // 测试注册的Controller是否能正常工作
        mockMvc.perform(get("/test/api/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"))
                .andExpect(handler().handlerType(TestController.class))
                .andExpect(handler().methodName("ping"));
    }
}
