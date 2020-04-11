package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.GenericDao;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.generic.NamedElement;
import com.code.to.learn.persistence.domain.model.IdServiceModel;
import com.code.to.learn.persistence.exception.basic.NameNotFoundException;
import com.code.to.learn.persistence.service.api.NamedElementService;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public abstract class NamedElementServiceImpl<E extends IdEntity<E> & NamedElement, M extends IdServiceModel & NamedElement> extends GenericServiceImpl<E, M>
        implements NamedElementService<E, M> {

    protected NamedElementServiceImpl(GenericDao<E> genericDao, ModelMapper modelMapper) {
        super(genericDao, modelMapper);
    }

    @Override
    public M findByName(String name) {
        Optional<E> entity = getGenericDao().findByField(NamedElement.NAME, name);
        if (!entity.isPresent()) {
            throw new NameNotFoundException(name);
        }
        return entity.map(this::toOutput).get();
    }
}
