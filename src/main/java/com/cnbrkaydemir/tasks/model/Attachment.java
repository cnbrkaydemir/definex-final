package com.cnbrkaydemir.tasks.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "attachments")
public class Attachment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "attachment_id")
    private UUID id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "path")
    private String path;

    @Enumerated(EnumType.STRING)
    private AttachmentType type;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
