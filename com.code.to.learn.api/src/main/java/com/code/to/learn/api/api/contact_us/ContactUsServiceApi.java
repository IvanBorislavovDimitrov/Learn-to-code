package com.code.to.learn.api.api.contact_us;

import com.code.to.learn.api.model.contact_us.ContactUsBindingModel;
import com.code.to.learn.api.model.contact_us.ContactUsResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContactUsServiceApi {

    ResponseEntity<ContactUsResponseModel> add(ContactUsBindingModel contactUsBindingModel);

    ResponseEntity<List<ContactUsResponseModel>> getAll();
}
