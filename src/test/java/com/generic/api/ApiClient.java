package com.generic.api;

import com.generic.base.BaseApi;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

@Component
public class ApiClient extends BaseApi {

    /**
     * @return the base URL (empty for client)
     */
    @Override
    protected String getUrl() {
        return "";
    }

    /**
     * @return the base path (empty for client)
     */
    @Override
    protected String getPath() {
        return "";
    }

    /**
     * Gets the request specification with a Bearer token.
     *
     * @param jwt the JSON Web Token
     * @return the request specification
     */
    public RequestSpecification getSpecification(String jwt) {
        return withBearer(jwt);
    }
}
