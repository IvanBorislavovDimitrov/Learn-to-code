package com.code.to.learn.api.api.contact_us;

import com.code.to.learn.api.model.contact_us.ContactUsBindingModel;
import com.code.to.learn.api.model.contact_us.ContactUsResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/contact-us", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContactUsRestController {

    private final ContactUsServiceApi contactUsServiceApi;

    @Autowired
    public ContactUsRestController(ContactUsServiceApi contactUsServiceApi) {
        this.contactUsServiceApi = contactUsServiceApi;
    }

    @GetMapping
    public ResponseEntity<List<ContactUsResponseModel>> getAll() {
        return contactUsServiceApi.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactUsResponseModel> add(@RequestBody @Valid ContactUsBindingModel contactUsBindingModel) {
        return contactUsServiceApi.add(contactUsBindingModel);
    }
}
