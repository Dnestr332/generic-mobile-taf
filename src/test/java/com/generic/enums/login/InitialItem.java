package com.generic.enums.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InitialItem implements LoginItem {

    INITIAL_TITLE(false),
    INITIAL_TAB(true);

    private final boolean clickable;
}
