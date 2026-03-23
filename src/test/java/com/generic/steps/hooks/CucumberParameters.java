package com.generic.steps.hooks;

import com.generic.enums.AppUser;
import com.generic.utils.EnumUtils;
import io.cucumber.java.ParameterType;

public class CucumberParameters {

    @ParameterType("(?i)TEST USER")
    public AppUser appUser(String value) {
        return EnumUtils.parse(AppUser.class, value);
    }
}
