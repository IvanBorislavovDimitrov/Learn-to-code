package com.code.to.learn.web.mail;

public class Email {

    private String recipient;
    private String title;
    private String content;

    private Email() {

    }

    public String getRecipient() {
        return recipient;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public static class Builder {

        private final Email email = new Email();

        public Builder setRecipient(String recipient) {
            email.recipient = recipient;
            return this;
        }

        public Builder setTitle(String title) {
            email.title = title;
            return this;
        }

        public Builder setContent(String content) {
            email.content = content;
            return this;
        }

        public Email build() {
            return email;
        }
    }

}
