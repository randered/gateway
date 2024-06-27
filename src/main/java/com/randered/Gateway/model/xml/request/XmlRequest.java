package com.randered.Gateway.model.xml.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JacksonXmlRootElement(localName = "command")
public class XmlRequest {

    private String id;

    private Current get;

    private History history;

    @Getter
    @Setter
    public static class Current {

        @JacksonXmlProperty(isAttribute = true)
        private String consumer;

        private String currency;
    }

    @Getter
    @Setter
    public static class History {

        @JacksonXmlProperty(isAttribute = true)
        private String consumer;

        private String currency;

        private Integer period;
    }
}
