package com.code.to.learn.process.steps;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class ValidateUserStep implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("Validating user");
    }
}
