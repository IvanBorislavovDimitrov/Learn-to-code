package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.impl.GenericDaoImpl;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.model.IdServiceModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

public class GenericServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private GenericDaoImpl<ExtendedIdEntity> genericDao;
    private GenericServiceImpl<ExtendedIdEntity, ExtendedIdServiceModel> genericServiceImpl;

    public GenericServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        genericServiceImpl = getGenericServiceImpl();
    }

    @Test
    public void testFindAll() {
        List<ExtendedIdServiceModel> all = genericServiceImpl.findAll();
        Assertions.assertEquals(0, all.size());
    }

    @Test
    public void testFindById() {
        ExtendedIdEntity extendedIdEntity = new ExtendedIdEntity();
        Mockito.when(genericDao.findById(anyString())).thenReturn(Optional.of(extendedIdEntity));
        ExtendedIdServiceModel extendedIdServiceModel = new ExtendedIdServiceModel();
        Mockito.when(modelMapper.map(extendedIdEntity, ExtendedIdServiceModel.class)).thenReturn(extendedIdServiceModel);
        IdServiceModel byId = genericServiceImpl.findById("123");
        Assertions.assertEquals("0", byId.getId());
    }

    @Test
    public void testSave() {
        ExtendedIdEntity extendedIdEntity = new ExtendedIdEntity();
        ExtendedIdServiceModel extendedIdServiceModel = new ExtendedIdServiceModel();
        Mockito.when(modelMapper.map(extendedIdServiceModel, ExtendedIdEntity.class)).thenReturn(extendedIdEntity);
        genericServiceImpl.save(extendedIdServiceModel);
        Mockito.verify(genericDao).persist(extendedIdEntity);
    }

    @Test
    public void testDeleteById() {
        ExtendedIdEntity extendedIdEntity = new ExtendedIdEntity();
        Mockito.when(genericDao.deleteById(anyString())).thenReturn(Optional.of(extendedIdEntity));
        ExtendedIdServiceModel extendedIdServiceModel = new ExtendedIdServiceModel();
        Mockito.when(modelMapper.map(extendedIdEntity, ExtendedIdServiceModel.class)).thenReturn(extendedIdServiceModel);
        IdServiceModel byId = genericServiceImpl.deleteById("123");
        Assertions.assertEquals("0", byId.getId());
    }

    @Test
    public void testDelete() {
        ExtendedIdEntity extendedIdEntity = new ExtendedIdEntity();
        Mockito.when(genericDao.deleteById(anyString())).thenReturn(Optional.of(extendedIdEntity));
        ExtendedIdServiceModel extendedIdServiceModel = new ExtendedIdServiceModel();
        extendedIdServiceModel.setId("123");
        Mockito.when(modelMapper.map(extendedIdEntity, ExtendedIdServiceModel.class)).thenReturn(extendedIdServiceModel);
        IdServiceModel byId = genericServiceImpl.delete(extendedIdServiceModel);
        Assertions.assertEquals("0", byId.getId());
    }

    @Test
    public void testMerge() {
        ExtendedIdEntity extendedIdEntity = new ExtendedIdEntity();
        Mockito.when(genericDao.findById(anyString())).thenReturn(Optional.of(extendedIdEntity));
        Mockito.when(genericDao.merge(any())).thenReturn(Optional.of(extendedIdEntity));
        ExtendedIdServiceModel extendedIdServiceModel = new ExtendedIdServiceModel();
        extendedIdServiceModel.setId("123");
        Mockito.when(modelMapper.map(extendedIdEntity, ExtendedIdServiceModel.class)).thenReturn(extendedIdServiceModel);
        Mockito.when(modelMapper.map(extendedIdServiceModel, ExtendedIdEntity.class)).thenReturn(extendedIdEntity);
        IdServiceModel byId = genericServiceImpl.merge(extendedIdServiceModel);
        Assertions.assertEquals("0", byId.getId());
    }

    @Test
    public void testCount() {
        Mockito.when(genericDao.count()).thenReturn(1L);
        long count = genericServiceImpl.count();
        Assertions.assertEquals(1L, count);
    }

    private GenericServiceImpl<ExtendedIdEntity, ExtendedIdServiceModel> getGenericServiceImpl() {
        return new GenericServiceImpl<ExtendedIdEntity, ExtendedIdServiceModel>(genericDao, modelMapper) {
            @Override
            protected Class<ExtendedIdEntity> getEntityClass() {
                return ExtendedIdEntity.class;
            }

            @Override
            protected Class<ExtendedIdServiceModel> getModelClass() {
                return ExtendedIdServiceModel.class;
            }
        };
    }

    private static class ExtendedIdEntity extends IdEntity<ExtendedIdEntity> {

        @Override
        public ExtendedIdEntity merge(ExtendedIdEntity genericEntity) {
            return this;
        }
    }

    private static class ExtendedIdServiceModel extends IdServiceModel {
        @Override
        public String getId() {
            return "0";
        }
    }
}
