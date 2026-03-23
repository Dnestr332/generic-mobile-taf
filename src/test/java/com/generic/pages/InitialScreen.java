package com.generic.pages;

import com.generic.base.BaseScreen;
import com.generic.enums.login.InitialItem;
import com.generic.interfaces.Screen;
import com.generic.mobile.ElementActions;
import io.appium.java_client.AppiumBy;
import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import static com.generic.mobile.TestEnvironment.isAndroid;

@Component
@ScenarioScope
public class InitialScreen extends BaseScreen<InitialItem> implements Screen {

    public InitialScreen(ElementActions elementActions) {
        super(elementActions);
    }

    @Override
    protected Class<InitialItem> itemType() {
        return InitialItem.class;
    }

    @Override
    protected By resolve(InitialItem item) {
        return switch (item) {
            case INITIAL_TITLE -> initialLabel();
            case INITIAL_TAB -> initialPhoneTab();
        };
    }

    /**
     * @return true if the initial title is visible
     */
    @Override
    public boolean isTitleVisible() {
        return isVisible(InitialItem.INITIAL_TITLE);
    }

    /**
     * @return the text of the initial title
     */
    @Override
    public String getTitleText() {
        return getText(InitialItem.INITIAL_TITLE);
    }

    // TODO: replace with real locators!
    private By initialLabel() {
        return isAndroid()
                ? By.id("DUMMY")
                : AppiumBy.iOSClassChain("DUMMY");
    }

    private By initialPhoneTab() {
        return isAndroid()
                ? By.xpath("DUMMY")
                : AppiumBy.accessibilityId("DUMMY");
    }
}
