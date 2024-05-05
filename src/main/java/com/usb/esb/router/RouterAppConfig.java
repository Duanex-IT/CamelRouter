package com.usb.esb.router;

import com.usb.esb.router.commportal.AddHeaderProcessor;
import com.usb.esb.router.commportal.AddSoapCredentialsProcessor;
import com.usb.esb.router.distancesales.RemoveSiebelHeaderProcessor;
import com.usb.esb.router.distancesales.UrlChoosePredicate;
import com.usb.esb.router.dmptickets.AccessHeaderExistsPredicate;
import com.usb.esb.router.dmptickets.AddTicketsAccessHeaderProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan({"com.usb.esb.router.commportal", "com.usb.esb.router.distancesales", "com.usb.esb.router.dmptickets"})
@PropertySources({
        @PropertySource("classpath:router.properties"),
        @PropertySource(value = "file:/opt/esb/config/router.properties", ignoreResourceNotFound = true)
})
public class RouterAppConfig extends SingleRouteCamelConfiguration implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(RouterAppConfig.class);

    public static final String COMMROUTER = "commrouter";
    public static final String CAMEL_URL_MAPPING = "camelUrlMapping";

    public static final String DMP_ACCESS_HEADER="access_token";
    public static final String DMP_ACCESS_HEADER_VALUE="89f26c0714a40738947b2051cc12b6d0a2b80dc0";

    @Value("${commportal.url}")
    private String commportalUrl;

    @Value("${itiny.url}")
    private String itinyUrl;

    @Value("${tickets.url}")
    private String ticketsUrl;

    @Value("${tickets.cards.url}")
    private String ticketsCardsUrl;

    @Value("${dmp.url}")
    private String dmpUrl;

    @Value("${distance.sales.url}")
    private String distanceSalesUrl;

    @Value("${distance.sales.esb.url}")
    private String distanceSalesEsbUrl;

    @Value("${krawly.url}")
    private String krawlyUrl;

    @Value("${credit.history.url}")
    private String creditHistoryUrl;

    @Autowired
    AddHeaderProcessor addHeaderProcessor;

    @Autowired
    RemoveSiebelHeaderProcessor removeSiebelHeaderProcessor;

    @Autowired
    AddSoapCredentialsProcessor addSoapCredsProcessor;

    @Autowired
    AddTicketsAccessHeaderProcessor addTicketsAccessHeaderProcessor;

    /**
     * Returns the CamelContext which support OSGi
     */
    @Override
    protected CamelContext createCamelContext() throws Exception {
        SpringCamelContext context = new SpringCamelContext(getApplicationContext());
        return context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Bean
    @Override
    public RouteBuilder route() {
        return new RouteBuilder() {
            public void configure() {
                // communal portal
                from("servlet:/?matchOnUriPrefix=true&servletName=CamelCommportalServlet")
                    .to(commportalUrl+"?bridgeEndpoint=true&throwExceptionOnFailure=false")
                    .process(addHeaderProcessor);

                //ITiny
                from("servlet:/?matchOnUriPrefix=true&servletName=CamelItinyServlet")
                        .to(itinyUrl+"?bridgeEndpoint=true&throwExceptionOnFailure=false");

                //Distance Sales
                from("servlet:/?matchOnUriPrefix=true&servletName=CamelDistanceSalesServlet")
                        .process(removeSiebelHeaderProcessor)
                        .choice()
                        .when(new UrlChoosePredicate())
                        .to(distanceSalesEsbUrl+"?bridgeEndpoint=true&throwExceptionOnFailure=false")
                        .otherwise().to(distanceSalesUrl + "?bridgeEndpoint=true&throwExceptionOnFailure=false");

                //CamelTicketsServlet
                from("servlet:/?matchOnUriPrefix=true&servletName=CamelTicketsServlet")
                        .process(addTicketsAccessHeaderProcessor)
                        .to("log:com.usb.esb.router.CamelTicketsServlet?showAll=true&multiline=true")
                        .to(ticketsUrl + "?bridgeEndpoint=true&throwExceptionOnFailure=false");
                from("servlet:/?matchOnUriPrefix=true&servletName=CamelTicketsCardsServlet")
                        .process(addTicketsAccessHeaderProcessor)
                        .to("log:com.usb.esb.router.CamelTicketsCardsServlet?showAll=true&multiline=true")
                        .to(ticketsCardsUrl + "?bridgeEndpoint=true&throwExceptionOnFailure=false");

                //CamelDmpServlet
                from("servlet:/?matchOnUriPrefix=true&servletName=CamelDmpServlet")
                        .to("log:com.usb.esb.router.CamelDmpServlet?showAll=true&multiline=true")
                        .choice()
                        .when(new AccessHeaderExistsPredicate())
                        .to(dmpUrl + "?bridgeEndpoint=true&throwExceptionOnFailure=false");

                //CamelKrawlyServlet
                from("servlet:/?matchOnUriPrefix=true&servletName=CamelKrawlyServlet")
                        .to("log:com.usb.esb.router.CamelKrawlyServlet?showAll=true&multiline=true")
                        .to(krawlyUrl + "?bridgeEndpoint=true&throwExceptionOnFailure=false");

                //CreditHistoryServlet
                from("servlet:/?matchOnUriPrefix=true&servletName=CreditHistoryServlet")
                        .to(creditHistoryUrl + "?bridgeEndpoint=true&throwExceptionOnFailure=false");

            }
        };
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


}
