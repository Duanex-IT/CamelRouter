package com.usb.esb.router;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RouterAppConfigTest {

    @Test
    public void testContext() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RouterAppConfig.class);
    }

}
