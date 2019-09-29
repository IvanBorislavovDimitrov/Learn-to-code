package com.code.to.learn.process.steps;

import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.UserService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("validateUserStep")
public class ValidateUserStep implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) {
        List<UserServiceModel> all = userService.getAll();
        System.out.println(all);
    }
}
