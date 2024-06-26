package com.randered.Gateway.model.json.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseJsonRequest {
    private String requestId;
    private Long timestamp;
    private String client;
    private String currency;
}
