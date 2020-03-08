package net.donationstore.dto;

import java.lang.reflect.Method;
import java.util.Map;

public class GatewayRequest {

    private Object body;
    private Method method;
    private Map<String, String> headers;
    private Map<String, String> params;
}
