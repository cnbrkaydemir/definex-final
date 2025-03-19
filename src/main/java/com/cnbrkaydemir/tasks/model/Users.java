package com.cnbrkaydemir.tasks.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "users")
@Data
public class Users extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToMany(mappedBy = "teamMembers")
    private List<Team> teams;

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Task> tasks;

    @OneToMany(mappedBy = "commentedBy", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

}
