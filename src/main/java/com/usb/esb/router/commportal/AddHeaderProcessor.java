package com.usb.esb.router.commportal;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AddHeaderProcessor implements Processor {

    @Value("${commportal.accesscontrol.header.origin.key}")
    private String originKey;
    @Value("${commportal.accesscontrol.header.origin.value}")
    private String originValue;

    @Value("${commportal.accesscontrol.header.methods.key}")
    private String methodsKey;
    @Value("${commportal.accesscontrol.header.methods.value}")
    private String methodsValue;

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().getHeaders().put(originKey, originValue);
        exchange.getIn().getHeaders().put(methodsKey, methodsValue);
    }
}
