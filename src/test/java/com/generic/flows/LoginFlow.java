package com.generic.flows;

import com.generic.base.BaseScreen;
import com.generic.context.ScenarioContext;
import com.generic.enums.AppUser;
import com.generic.enums.Context;
import com.generic.enums.login.LoginItem;
import com.generic.enums.login.LoginState;
import com.generic.resolvers.LoginScreenResolver;
import io.cucumber.spring.ScenarioScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.generic.logs.LogStyles.*;

@Slf4j
@Component
@ScenarioScope
@RequiredArgsConstructor
public class LoginFlow {

    private final LoginScreenResolver screenResolver;
    private final ScenarioContext scenarioContext;

    private BaseScreen<?> resolve(LoginState state) {
        return screenResolver.resolveScreen(state);
    }

    /**
     * Taps on a login item.
     *
     * @param state the login state
     * @param item  the login item
     */
    public void tap(LoginState state, LoginItem item) {
        BaseScreen<?> screen = resolve(state);
        screen.click(item);
    }

    /**
     * Types a value into a login item.
     *
     * @param state the login state
     * @param item  the login item
     * @param value the value to type
     */
    public void type(LoginState state, LoginItem item, String value) {
        BaseScreen<?> screen = resolve(state);
        screen.type(item, value);
    }

    /**
     * Performs a login flow for a specific user.
     *
     * @param user the app user
     */
    public void loginAs(AppUser user) {
        log.info(">>>  {} {} Logging in as {}{}", INFO_SHORT, LENNY, GREEN, user);
        String phone = user.getPhone();
        scenarioContext.set(Context.PHONE_NUMBER, phone);

        // TODO: implement login method
        //loginWithPhoneNumber(phone);
    }
}
