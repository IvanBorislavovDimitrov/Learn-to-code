package com.code.to.learn.web.environment;

public final class EnvironmentGetter {

    private EnvironmentGetter() {

    }

    public static String getVariable(String name) {
        return System.getenv(name);
    }
}
