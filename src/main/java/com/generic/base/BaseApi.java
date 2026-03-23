package com.generic.base;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseApi {

    /**
     * @return the base URL for the API
     */
    protected abstract String getUrl();

    /**
     * @return the base path for the API
     */
    protected abstract String getPath();

    /**
     * Creates a base request specification with common settings.
     *
     * @return the request specification
     */
    public RequestSpecification baseSpec() {
        return RestAssured.given()
                .baseUri(getUrl())
                .basePath(getPath())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }

    /**
     * Creates a request specification with a Bearer token.
     *
     * @param jwt the JSON Web Token
     * @return the request specification with authorization header
     */
    protected RequestSpecification withBearer(String jwt) {
        return baseSpec()
                .header("Authorization", "Bearer " + jwt);
    }
}
