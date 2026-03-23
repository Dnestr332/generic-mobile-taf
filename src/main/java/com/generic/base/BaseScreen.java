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

    /**
     * @return the class of the enum representing screen items
     */
    protected abstract Class<I> itemType();

    /**
     * Resolves an enum item to its corresponding By locator.
     *
     * @param item the screen item enum
     * @return the locator
     */
    protected abstract By resolve(I item);

    /**
     * Checks if the screen supports a specific item.
     *
     * @param item the screen item
     * @return true if the item is supported, false otherwise
     */
    public boolean supports(I item) {
        try {
            resolve(item);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    //region CORE METHODS
    /**
     * Clicks on a screen item.
     *
     * @param item the screen item to click
     */
    public void click(I item) {
        if (!item.isClickable()) {
            throw new IllegalStateException(
                    "%s is not clickable on %s".formatted(item, getClass().getSimpleName())
            );
        }
        elementActions.click(resolve(item));
    }

    /**
     * Types a value into a screen item.
     *
     * @param item  the screen item to type into
     * @param value the value to type
     */
    public void type(I item, String value) {
        elementActions.type(resolve(item), value);
    }

    /**
     * Gets the text from a screen item.
     *
     * @param item the screen item
     * @return the text from the item
     */
    public String getText(I item) {
        return elementActions.text(resolve(item));
    }

    /**
     * Checks if a screen item is visible.
     *
     * @param item the screen item
     * @return true if visible, false otherwise
     */
    public boolean isVisible(I item) {
        return elementActions.isVisible(resolve(item));
    }

    /**
     * Checks if a screen item is visible (quick check without long wait).
     *
     * @param item the screen item
     * @return true if visible, false otherwise
     */
    public boolean isQuickVisible(I item) {
        return elementActions.isQuickVisible(resolve(item));
    }

    /**
     * Checks if a screen item is enabled.
     *
     * @param item the screen item
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled(I item) {
        return elementActions.isEnabled(resolve(item));
    }

    /**
     * Finds a web element corresponding to a screen item.
     *
     * @param item the screen item
     * @return the web element
     */
    public WebElement find(I item) {
        return elementActions.find(resolve(item));
    }

    /**
     * Finds a list of web elements corresponding to a screen item.
     *
     * @param item the screen item
     * @return the list of web elements
     */
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

    /**
     * Clicks on a screen item through Clickable interface.
     *
     * @param item the clickable item
     */
    public void click(Clickable item) {
        click(cast(item));
    }

    /**
     * Types a value into a screen item through Clickable interface.
     *
     * @param item  the clickable item
     * @param value the value to type
     */
    public void type(Clickable item, String value) {
        type(cast(item), value);
    }

    /**
     * Gets text from a screen item through Clickable interface.
     *
     * @param item the clickable item
     * @return the text from the item
     */
    public String getText(Clickable item) {
        return getText(cast(item));
    }

    /**
     * Checks if a screen item is visible through Clickable interface.
     *
     * @param item the clickable item
     * @return true if visible, false otherwise
     */
    public boolean isVisible(Clickable item) {
        return isVisible(cast(item));
    }

    /**
     * Checks if a screen item is visible through Clickable interface (quick check).
     *
     * @param item the clickable item
     * @return true if visible, false otherwise
     */
    public boolean isQuickVisible(Clickable item) {
        return isQuickVisible(cast(item));
    }

    /**
     * Checks if a screen item is enabled through Clickable interface.
     *
     * @param item the clickable item
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled(Clickable item) {
        return isEnabled(cast(item));
    }

    /**
     * Finds a web element through Clickable interface.
     *
     * @param item the clickable item
     * @return the web element
     */
    public WebElement find(Clickable item) {
        return find(cast(item));
    }

    /**
     * Finds a list of web elements through Clickable interface.
     *
     * @param item the clickable item
     * @return the list of web elements
     */
    public List<WebElement> findList(Clickable item) {
        return findList(cast(item));
    }
    //endregion
}
