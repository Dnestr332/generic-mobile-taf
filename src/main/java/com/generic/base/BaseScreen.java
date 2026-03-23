package com.generic.base;

import com.generic.interfaces.Clickable;
import com.generic.mobile.ElementActions;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseScreen<I extends Enum<I> & Clickable> {

    protected final ElementActions elementActions;

    protected abstract Class<I> itemType();
    protected abstract By resolve(I item);

    public Class<I> getItemType() {
        return itemType();
    }

    public boolean supports(I item) {
        try {
            resolve(item);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    //region CORE METHODS
    public void click(I item) {
        if (!item.isClickable()) {
            throw new IllegalStateException(
                    "%s is not clickable on %s".formatted(item, getClass().getSimpleName())
            );
        }
        elementActions.click(resolve(item));
    }

    public void type(I item, String value) {
        elementActions.type(resolve(item), value);
    }

    public String getText(I item) {
        return elementActions.text(resolve(item));
    }

    public boolean isVisible(I item) {
        return elementActions.isVisible(resolve(item));
    }

    public boolean isQuickVisible(I item) {
        return elementActions.isQuickVisible(resolve(item));
    }

    public boolean isEnabled(I item) {
        return elementActions.isEnabled(resolve(item));
    }

    public WebElement find(I item) {
        return elementActions.find(resolve(item));
    }

    public List<WebElement> findList(I item) {
        return elementActions.findList(resolve(item));
    }
    //endregion

    //region CLICKABLE API
    private I cast(Clickable item) {
        if (!itemType().isInstance(item)) {
            throw new IllegalStateException(
                    "%s does not belong to %s".formatted(item, getClass().getSimpleName())
            );
        }
        return (I) item;
    }

    public void click(Clickable item) {
        click(cast(item));
    }

    public void type(Clickable item, String value) {
        type(cast(item), value);
    }

    public String getText(Clickable item) {
        return getText(cast(item));
    }

    public boolean isVisible(Clickable item) {
        return isVisible(cast(item));
    }

    public boolean isQuickVisible(Clickable item) {
        return isQuickVisible(cast(item));
    }

    public boolean isEnabled(Clickable item) {
        return isEnabled(cast(item));
    }

    public WebElement find(Clickable item) {
        return find(cast(item));
    }

    public List<WebElement> findList(Clickable item) {
        return findList(cast(item));
    }
    //endregion
}
