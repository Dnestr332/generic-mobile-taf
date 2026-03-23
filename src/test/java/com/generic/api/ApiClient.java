package com.generic.api;

import com.generic.base.BaseApi;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

@Component
public class ApiClient extends BaseApi {

    @Override
    protected String getUrl() {
        return "";
    }

    @Override
    protected String getPath() {
        return "";
    }

    public RequestSpecification getSpecification(String jwt) {
        return withBearer(jwt);
    }
}
