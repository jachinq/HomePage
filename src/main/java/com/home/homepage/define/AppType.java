package com.home.homepage.define;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public enum AppType {
    Sys(1),
    App(2),
    Bookmark(3);

    private final int value;

    AppType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AppType fromValue(int value) {
        for (AppType type : AppType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

    public static List<Integer> valueList() {
        List<Integer> values = new ArrayList<>();
        Arrays.stream(AppType.values()).map(AppType::getValue).forEach(values::add);
        return values;
    }

}
