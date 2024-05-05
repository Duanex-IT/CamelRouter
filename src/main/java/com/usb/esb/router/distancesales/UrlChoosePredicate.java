package com.usb.esb.router.distancesales;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.component.http.HttpMessage;

public class UrlChoosePredicate implements Predicate {
    @Override
    public boolean matches(Exchange exchange) {
        String uri = ((HttpMessage) exchange.getIn()).getRequest().getRequestURI();
        if (uri.startsWith("/esb/camel/distancesales/rest/")
                || uri.startsWith("/esb/camel/distancesales/soap/")) {
            return true;
        } else {
            return false;
        }
    }
}
