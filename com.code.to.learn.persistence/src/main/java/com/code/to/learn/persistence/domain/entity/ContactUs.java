package com.code.to.learn.persistence.domain.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "contactus")
public class ContactUs extends IdEntity<ContactUs> {

    @Basic
    private String username;
    @Basic
    private String email;
    @Basic
    private String subject;
    @Basic
    private String message;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public ContactUs merge(ContactUs contactUs) {
        setUsername(contactUs.getUsername());
        setEmail(contactUs.getEmail());
        setSubject(contactUs.getSubject());
        setMessage(contactUs.getMessage());
        return this;
    }
}
