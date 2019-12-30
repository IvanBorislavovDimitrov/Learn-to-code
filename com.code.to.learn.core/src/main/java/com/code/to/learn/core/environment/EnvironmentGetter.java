package com.code.to.learn.core.environment;

public final class EnvironmentGetter {

    private EnvironmentGetter() {

    }

    public static String getVariable(String name) {
        return System.getenv(name);
    }
}
