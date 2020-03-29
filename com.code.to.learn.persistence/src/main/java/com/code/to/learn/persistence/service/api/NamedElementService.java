package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.generic.NamedElement;
import com.code.to.learn.persistence.domain.model.IdServiceModel;

import java.util.Optional;

public interface NamedElementService<E extends IdEntity<E> & NamedElement, M extends IdServiceModel & NamedElement> {

    Optional<M> findByName(String name);
}
