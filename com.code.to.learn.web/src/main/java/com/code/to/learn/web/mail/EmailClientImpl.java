package com.code.to.learn.web.mail;

import com.code.to.learn.core.environment.ApplicationConfiguration;
import com.code.to.learn.core.util.ResilientExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import static com.code.to.learn.web.constants.Constants.MAIL_SMPT_START_TLS_ENABLED;
import static com.code.to.learn.web.constants.Constants.MAIL_SMTP_AUTH;
import static com.code.to.learn.web.constants.Constants.MAIL_SMTP_HOST;
import static com.code.to.learn.web.constants.Constants.MAIL_SMTP_PORT;

@Component
public class EmailClientImpl implements EmailClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailClientImpl.class);

    private final ApplicationConfiguration applicationConfiguration;
    private final ResilientExecutor resilientExecutor;
    private final ExecutorService executorService;

    @Autowired
    public EmailClientImpl(ApplicationConfiguration applicationConfiguration, ResilientExecutor resilientExecutor, ExecutorService executorService) {
        this.applicationConfiguration = applicationConfiguration;
        this.resilientExecutor = resilientExecutor;
        this.executorService = executorService;
    }

    @Override
    public void sendAsync(Email email) {
        executorService.submit(() -> {
            try {
                Message message = new MimeMessage(getSession());
                message.setFrom(new InternetAddress(applicationConfiguration.getSmtpUsername()));
                message.setRecipients(
                        Message.RecipientType.TO, InternetAddress.parse(email.getRecipient()));
                message.setSubject(email.getTitle());
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(email.getContent(), MediaType.TEXT_HTML_VALUE);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);
                message.setContent(multipart);
                resilientExecutor.executeWithRetry(() -> {
                    Transport.send(message);
                    return null;
                });
            } catch (MessagingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
    }

    private Session getSession() {
        return Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(applicationConfiguration.getSmtpUsername(), applicationConfiguration.getSmtpPassword());
            }
        });
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put(MAIL_SMTP_HOST, applicationConfiguration.getSmtpHost());
        properties.put(MAIL_SMTP_PORT, applicationConfiguration.getSmtpPort());
        properties.put(MAIL_SMTP_AUTH, applicationConfiguration.getAuthEnabled());
        properties.put(MAIL_SMPT_START_TLS_ENABLED, applicationConfiguration.getSmtpStartTlsEnabled());
        return properties;
    }
}
