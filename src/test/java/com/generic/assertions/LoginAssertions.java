package com.generic.assertions;

import com.generic.base.BaseAssertion;
import com.generic.base.BaseScreen;
import com.generic.enums.login.LoginItem;
import com.generic.enums.login.LoginState;
import com.generic.enums.main.AssertionState;
import com.generic.enums.main.ButtonState;
import com.generic.enums.main.VisibleState;
import com.generic.interfaces.Screen;
import com.generic.resolvers.LoginScreenResolver;
import io.cucumber.spring.ScenarioScope;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriverException;
import org.springframework.stereotype.Component;

import java.util.function.BooleanSupplier;

@Component
@ScenarioScope
@RequiredArgsConstructor
public class LoginAssertions extends BaseAssertion {

    private final LoginScreenResolver screenResolver;

    private BaseScreen resolve(LoginState state) {
        return screenResolver.resolveScreen(state);
    }

    public void verifyVisible(LoginState state, LoginItem item,
                              AssertionState assertion, VisibleState visible) {

        BaseScreen<?> screen = resolve(state);

        BooleanSupplier condition = switch (visible) {
            case VISIBLE -> () -> screen.isVisible(item);
            case NOT_VISIBLE -> () -> !screen.isQuickVisible(item);
        };

        String message = "❌ %s | %s should be %s".formatted(
                state.name(),
                item,
                visible.name().replace("_", " ").toLowerCase()
        );

        switch (assertion) {
            case STRICTLY -> hardlyCondition(condition, message);
            case SOFTLY -> softlyCondition(condition, message);
        }
    }

    public void verifyTextEquals(LoginState state, LoginItem item,
                                 AssertionState assertion, String expected) {
        BaseScreen<?> screen = resolve(state);
        String message = "❌ %s | %s text validation".formatted(state.name(), item);

        switch (assertion) {
            case STRICTLY -> hardlyEquals(() -> screen.getText(item), expected, message);
            case SOFTLY -> softlyEquals(() -> screen.getText(item), expected, message);
        }
    }

    public void verifyTextContains(LoginState state, LoginItem item, String partial) {
        BaseScreen<?> screen = resolve(state);

        softlyCondition(() -> screen.getText(item).contains(partial),
                "❌ %s | %s should contain <%s>".formatted(state.name(), item, partial));
    }

    public void verifyItemState(LoginState state, LoginItem item, ButtonState buttonState) {
        BaseScreen<?> screen = resolve(state);

        softlyCondition(
                () -> switch (buttonState) {
                    case ENABLED -> screen.isEnabled(item);
                    case DISABLED -> !screen.isEnabled(item);
                }, "❌ %s should be %s".formatted(item, buttonState)
        );
    }

    public void verifyRedirected(LoginState state, String condition) {
        Screen screen = (Screen) resolve(state);

        boolean redirected = false;

        try {
            redirected = screen.isTitleVisible();
        } catch (WebDriverException ignored) {}

        boolean expected = condition.equalsIgnoreCase("is");
        boolean finalRedirected = redirected;

        hardlyCondition(
                () -> finalRedirected == expected,
                "❌ Expected redirect %s to %s screen"
                        .formatted(expected ? "" : "NOT", state.name())
        );
    }
}
