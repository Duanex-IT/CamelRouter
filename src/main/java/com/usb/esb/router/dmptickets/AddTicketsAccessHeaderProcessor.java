package com.usb.esb.router.dmptickets;

import com.usb.esb.router.RouterAppConfig;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service
public class AddTicketsAccessHeaderProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().getHeaders().put(RouterAppConfig.DMP_ACCESS_HEADER, RouterAppConfig.DMP_ACCESS_HEADER_VALUE);
    }

}
