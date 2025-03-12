package com.cnbrkaydemir.tasks.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "modified_date")
    private Date modifiedDate;
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
    @Column(name = "deleted_at")
    private Date deleted_at;

    public void safeDelete() {
        this.deleted = true;
        this.deleted_at = new Date();
    }

}
