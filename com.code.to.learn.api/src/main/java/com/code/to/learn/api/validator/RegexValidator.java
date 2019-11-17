package com.code.to.learn.api.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexValidator {

    boolean regexMatches(String regex, String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }
}
