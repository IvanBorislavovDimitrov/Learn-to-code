package com.code.to.learn.api.api.contact_us;

import com.code.to.learn.api.model.contact_us.ContactUsBindingModel;
import com.code.to.learn.api.model.contact_us.ContactUsResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/contact-us", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContactUsRestController {

    private final ContactUsServiceApi contactUsServiceApi;

    @Autowired
    public ContactUsRestController(ContactUsServiceApi contactUsServiceApi) {
        this.contactUsServiceApi = contactUsServiceApi;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactUsResponseModel> add(@RequestBody @Valid ContactUsBindingModel contactUsBindingModel) {
        return contactUsServiceApi.add(contactUsBindingModel);
    }
}
