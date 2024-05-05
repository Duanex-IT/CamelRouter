package com.usb.esb.router.distancesales;

import com.usb.distancesales.common.DsConstants;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service
public class RemoveSiebelHeaderProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().getHeaders().remove(DsConstants.SIEBEL_USER_HEADER);
    }

}
