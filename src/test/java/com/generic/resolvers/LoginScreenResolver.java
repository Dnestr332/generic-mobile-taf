package com.generic.resolvers;

import com.generic.base.BaseScreen;
import com.generic.enums.login.LoginState;
import com.generic.pages.InitialScreen;
import io.cucumber.spring.ScenarioScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
@RequiredArgsConstructor
public class LoginScreenResolver {

    private final InitialScreen initialScreen;

    public BaseScreen<?> resolveScreen(LoginState state) {
        return switch (state) {
            case INITIAL -> initialScreen;
        };
    }
}
