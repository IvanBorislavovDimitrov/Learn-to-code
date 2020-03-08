package com.code.to.learn.persistence.domain.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class GenericEntity<T extends GenericEntity<T>> {

    public static final String ID = "id";

    @Id
    @GenericGenerator(name = "uuid-string", strategy = "uuid")
    @GeneratedValue(generator = "uuid-string")
    @Column(name = ID, unique = true, nullable = false, updatable = false)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract T merge(T genericEntity);
}
