package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.GenericDao;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.generic.NamedElement;
import com.code.to.learn.persistence.domain.model.IdServiceModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class NamedElementServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private GenericDao<ExtendedNamedElement> genericDao;
    private NamedElementServiceImpl<ExtendedNamedElement, ExtendedIdServiceModel> namedElementService;

    public NamedElementServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        namedElementService = new ExtendedNamedElementServiceImpl(genericDao, modelMapper);
    }

    @Test
    public void testGetByName() {
        ExtendedNamedElement namedElement = Mockito.mock(ExtendedNamedElement.class);
        Mockito.when(namedElement.getName()).thenReturn("123");
        Mockito.when(genericDao.findByField(NamedElement.NAME, "123")).thenReturn(Optional.of(namedElement));
        ExtendedIdServiceModel ex1 = Mockito.mock(ExtendedIdServiceModel.class);
        Mockito.when(ex1.getName()).thenReturn("123");
        Mockito.when(modelMapper.map(namedElement, ExtendedIdServiceModel.class)).thenReturn(ex1);
        ExtendedIdServiceModel ex = namedElementService.findByName("123");
        Assertions.assertEquals("123", ex.getName());
    }

    private static class ExtendedNamedElementServiceImpl extends NamedElementServiceImpl<ExtendedNamedElement, ExtendedIdServiceModel> {

        protected ExtendedNamedElementServiceImpl(GenericDao<ExtendedNamedElement> genericDao, ModelMapper modelMapper) {
            super(genericDao, modelMapper);
        }

        @Override
        protected Class<ExtendedNamedElement> getEntityClass() {
            return ExtendedNamedElement.class;
        }

        @Override
        protected Class<ExtendedIdServiceModel> getModelClass() {
            return ExtendedIdServiceModel.class;
        }
    }

    private static class ExtendedNamedElement extends IdEntity<ExtendedNamedElement> implements NamedElement {

        @Override
        public ExtendedNamedElement merge(ExtendedNamedElement genericEntity) {
            return this;
        }

        @Override
        public String getName() {
            return "123";
        }
    }

    private static class ExtendedIdServiceModel extends IdServiceModel implements NamedElement {
        @Override
        public String getId() {
            return "0";
        }

        @Override
        public String getName() {
            return "1";
        }
    }

}
