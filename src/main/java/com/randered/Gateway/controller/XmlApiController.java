package com.randered.Gateway.controller;

import com.randered.Gateway.model.xml.response.XmlResponse;
import com.randered.Gateway.service.core.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xml_api")
public class XmlApiController {

    @Autowired
    private ResponseService service;

    @PostMapping(value = "/command", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<XmlResponse> handleXmlCommand(@RequestBody String xmlRequest) {
        return service.processXmlRequest(xmlRequest);
    }
}
