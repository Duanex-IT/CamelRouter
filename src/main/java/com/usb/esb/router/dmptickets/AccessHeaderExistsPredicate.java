package com.usb.esb.router.dmptickets;

import com.usb.esb.router.RouterAppConfig;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.component.http.HttpMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessHeaderExistsPredicate implements Predicate {

    private static final Logger log = LoggerFactory.getLogger(AccessHeaderExistsPredicate.class);

    @Override
    public boolean matches(Exchange exchange) {
        String accessToken = (String) exchange.getIn().getHeader(RouterAppConfig.DMP_ACCESS_HEADER);
        if (StringUtils.isEmpty(accessToken)) {
            log.error("No "+RouterAppConfig.DMP_ACCESS_HEADER+" header found");
            ((HttpMessage) exchange.getIn()).getResponse().setStatus(401);
            return false;
        } else if (!RouterAppConfig.DMP_ACCESS_HEADER_VALUE.equals(accessToken)){
            log.error("Header "+RouterAppConfig.DMP_ACCESS_HEADER+" is incorrect, value received = "+accessToken);
            ((HttpMessage) exchange.getIn()).getResponse().setStatus(403);
            return false;
        } else {
            return true;
        }
    }
}
