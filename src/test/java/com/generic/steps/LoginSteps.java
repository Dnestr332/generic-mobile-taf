package com.generic.steps;

import com.generic.assertions.LoginAssertions;
import com.generic.enums.AppUser;
import com.generic.flows.LoginFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginSteps {

    private final LoginFlow loginFlow;
    private final LoginAssertions loginAssertions;

    @Given("user is logged in as {appUser}")
    public void userIsLoggedIn(AppUser user) {
        loginFlow.loginAs(user);
    }

    @Then("user is redirected to the main screen")
    public void userIsRedirectedToTheMainScreen() {
        // TODO: implement assertion
        //loginAssertions.verifyRedirected();
    }
}
