package com.usb.esb.router.commportal;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.CastUtils;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddSoapCredentialsProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String soapHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?><wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\""+
        "xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><wsse:UsernameToken wsu:Id=\"UsernameToken-50\"><wsse:Username>"
                + "OBPT_057"
                + "</wsse:Username><wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">"
                + "Hh222222" + "</wsse:Password></wsse:UsernameToken></wsse:Security>";

        //Add wsse security header to the exchange

        addSoapHeader(exchange, soapHeader);

    }

    public void addSoapHeader(Exchange exchange,String soapHeader){

        List<SoapHeader> soapHeaders = CastUtils.cast((List<?>) exchange.getIn().getHeader(Header.HEADER_LIST));
        SoapHeader newHeader;

        if(soapHeaders == null){

            soapHeaders = new ArrayList<SoapHeader>();
        }

        try {
            newHeader = new SoapHeader(new QName("soapHeader"), newDocumentFromInputStream(new StringReader(soapHeader)).getDocumentElement());
            newHeader.setDirection(Header.Direction.DIRECTION_OUT);

            soapHeaders.add(newHeader);

            exchange.getIn().setHeader(Header.HEADER_LIST, soapHeaders);

        } catch (Exception e) {
            //log error
        }
    }

    public static Document newDocumentFromInputStream(Reader in) {
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document ret = null;

        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            ret = builder.parse(new InputSource(in));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
