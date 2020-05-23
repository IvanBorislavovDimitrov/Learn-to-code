package com.code.to.learn.web.scheduled_jobs;

import com.code.to.learn.core.environment.ApplicationConfiguration;
import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.User;
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
    private final UserDao userDao;
    private final ApplicationConfiguration applicationConfiguration;

    @Autowired
    public NewestCoursesDailyEmailSender(EmailClient emailClient, UserDao userDao, ApplicationConfiguration applicationConfiguration) {
        this.emailClient = emailClient;
        this.userDao = userDao;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Scheduled(cron = THREE_AM_EVERY_DAY)
    @Async
    public void sendDailyNewCourses() {
        int page = 0;
        List<User> userServiceModels = userDao.findUsersByPage(page, MAX_USERS_BY_PAGE);
        while (!userServiceModels.isEmpty()) {
            sendDailyEmails(userServiceModels);
            userServiceModels = userDao.findUsersByPage(++page, MAX_USERS_BY_PAGE);
        }
    }

    private void sendDailyEmails(List<User> users) {
        for (User user : users) {
            Email dailyEmail = new Email.Builder()
                    .setTitle(CHECKOUT_OUR_NEW_COURSE)
                    .setContent(MessageFormat.format(VISIT_OUR_SITE_AND_CHECK_THE_COURSES, applicationConfiguration.getClientUrl()))
                    .setRecipient(user.getEmail())
                    .build();
            emailClient.sendAsync(dailyEmail);
        }
    }
}
