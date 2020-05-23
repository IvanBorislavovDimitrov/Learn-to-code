package com.code.to.learn.web.api_facade;

import com.code.to.learn.api.api.contact_us.ContactUsServiceApi;
import com.code.to.learn.api.model.contact_us.ContactUsBindingModel;
import com.code.to.learn.api.model.contact_us.ContactUsResponseModel;
import com.code.to.learn.persistence.domain.model.ContactUsServiceModel;
import com.code.to.learn.persistence.service.api.ContactUsService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactUsServiceApiFacade extends ExtendableMapper<ContactUsServiceModel, ContactUsResponseModel> implements ContactUsServiceApi {

    private final ContactUsService contactUsService;

    @Autowired
    protected ContactUsServiceApiFacade(ModelMapper modelMapper, ContactUsService contactUsService) {
        super(modelMapper);
        this.contactUsService = contactUsService;
    }


    @Override
    public ResponseEntity<ContactUsResponseModel> add(ContactUsBindingModel contactUsBindingModel) {
        ContactUsServiceModel contactUsServiceModel = getMapper().map(contactUsBindingModel, ContactUsServiceModel.class);
        contactUsService.save(contactUsServiceModel);
        return ResponseEntity.ok(toOutput(contactUsServiceModel));
    }

    @Override
    public ResponseEntity<List<ContactUsResponseModel>> getAll() {
        List<ContactUsServiceModel> contactUsServiceAll = contactUsService.findAll();
        return ResponseEntity.ok(toOutput(contactUsServiceAll));
    }

    @Override
    protected Class<ContactUsServiceModel> getInputClass() {
        return ContactUsServiceModel.class;
    }

    @Override
    protected Class<ContactUsResponseModel> getOutputClass() {
        return ContactUsResponseModel.class;
    }
}
