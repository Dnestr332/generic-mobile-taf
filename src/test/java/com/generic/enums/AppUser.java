package com.generic.enums;

import com.generic.config.TestDataReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.generic.mobile.TestEnvironment.isAndroid;

@Getter
@RequiredArgsConstructor
public enum AppUser {

    TEST_USER("android.phone.number", "ios.phone.number");

    private final String androidKey;
    private final String iosKey;

    public String getPhone() {
        String key = isAndroid() ? androidKey : iosKey;
        return TestDataReader.getProperty(key);
    }
}
