package com.usb.esb.router.commportal;

import com.usb.esb.router.RouterAppConfig;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = { RouterAppConfig.class },
        loader = AnnotationConfigContextLoader.class )
public class AddHeaderProcessorTest {

    @Autowired
    private AddHeaderProcessor processor;

    @Autowired
    private CamelContext context;

    @Value("${commportal.accesscontrol.header.origin.key}")
    private String key;
    @Value("${commportal.accesscontrol.header.origin.value}")
    private String value;

    @Value("${commportal.accesscontrol.header.methods.key}")
    private String methodsKey;

    @Test
    public void testProcess() throws Exception {
        Exchange exc = new DefaultExchange(context);
        processor.process(exc);

        assertEquals(exc.getIn().getHeader(key), value);
        assertEquals(exc.getIn().getHeader(methodsKey), "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
    }

}
