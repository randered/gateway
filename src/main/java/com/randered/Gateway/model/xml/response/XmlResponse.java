package com.randered.Gateway.model.xml.response;

import com.randered.Gateway.entity.Rate;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlResponse {

    @XmlElement
    private String message;

    @XmlElement(name = "baseCurrency")
    private String baseCurrency;

    @XmlElementWrapper(name = "exchangeRates")
    @XmlElement(name = "rates")
    private List<Rate> exchangeRates;
}
