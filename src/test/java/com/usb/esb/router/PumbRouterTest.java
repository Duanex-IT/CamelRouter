package com.usb.esb.router;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RouterAppConfig.class })
public class PumbRouterTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
//                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    @Ignore
    public void requestProtectedResourceRequiresAuthentication() throws Exception {
        assertNotEquals(mvc.perform(get("/camel/pumb/")).andReturn().getResponse().getStatus(), 404);
//                .andExpect(redirectedUrl("http://localhost/login"));
    }

}
