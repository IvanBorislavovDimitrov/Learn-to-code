package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.generic.NamedElement;

import java.util.Optional;

public interface NamedElementDao<E extends IdEntity<E> & NamedElement> {

    Optional<E> findByName(String name);
}
