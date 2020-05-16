package com.code.to.learn.web.scheduled_jobs;

import com.code.to.learn.core.environment.ApplicationConfiguration;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.web.mail.Email;
import com.code.to.learn.web.mail.EmailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

import static com.code.to.learn.web.constants.Constants.MAX_USERS_BY_PAGE;
import static com.code.to.learn.web.constants.Constants.THREE_AM_EVERY_DAY;
import static com.code.to.learn.web.constants.Messages.CHECKOUT_OUR_NEW_COURSE;
import static com.code.to.learn.web.constants.Messages.VISIT_OUR_SITE_AND_CHECK_THE_COURSES;

@EnableAsync
@Component
public class NewestCoursesDailyEmailSender {

    private final EmailClient emailClient;
    private final UserService userService;
    private final ApplicationConfiguration applicationConfiguration;

    @Autowired
    public NewestCoursesDailyEmailSender(EmailClient emailClient, UserService userService, ApplicationConfiguration applicationConfiguration) {
        this.emailClient = emailClient;
        this.userService = userService;
        this.applicationConfiguration = applicationConfiguration;
    }

    // TODO: Possible connection leak, db session is not closed!!!
    @Scheduled(cron = THREE_AM_EVERY_DAY)
    @Async
    public void sendDailyNewCourses() {
        int page = 1;
        List<UserServiceModel> userServiceModels = userService.findUsersByPage(page, MAX_USERS_BY_PAGE);
        while (!userServiceModels.isEmpty()) {
            sendDailyEmails(userServiceModels);
            userServiceModels = userService.findUsersByPage(++page, MAX_USERS_BY_PAGE);
        }
    }

    private void sendDailyEmails(List<UserServiceModel> userServiceModels) {
        for (UserServiceModel userServiceModel : userServiceModels) {
            Email dailyEmail = new Email.Builder()
                    .setTitle(CHECKOUT_OUR_NEW_COURSE)
                    .setContent(MessageFormat.format(VISIT_OUR_SITE_AND_CHECK_THE_COURSES, applicationConfiguration.getClientUrl()))
                    .setRecipient(userServiceModel.getEmail())
                    .build();
            emailClient.sendAsync(dailyEmail);
        }
    }
}
