package com.generic.enums;

import lombok.Getter;

@Getter
public enum Platform {

    ANDROID(true, false),
    IOS(false, true);

    private final boolean android;
    private final boolean ios;

    Platform(boolean android, boolean ios) {
        this.android = android;
        this.ios = ios;
    }
}
