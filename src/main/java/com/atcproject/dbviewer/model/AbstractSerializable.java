package com.atcproject.dbviewer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.ZonedDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@MappedSuperclass
public abstract class AbstractSerializable implements Serializable {

    // common system list of columns for all entities
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    protected ZonedDateTime createdDate = ZonedDateTime.now();

    // can be be extended
}
