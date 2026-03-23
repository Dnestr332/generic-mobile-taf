package com.generic.base;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseApi {

    protected abstract String getUrl();
    protected abstract String getPath();

    public RequestSpecification baseSpec() {
        return RestAssured.given()
                .baseUri(getUrl())
                .basePath(getPath())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }

    protected RequestSpecification withBearer(String jwt) {
        return baseSpec()
                .header("Authorization", "Bearer " + jwt);
    }
}
