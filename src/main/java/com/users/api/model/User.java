package com.users.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@SecondaryTable(name = "user_details", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private boolean isEnabled;

    @Column(table = "user_details")
    private String email;

    @Column(table = "user_details")
    private String firstName;

    @Column(table = "user_details")
    private String lastName;

    @Column(table = "user_details")
    private String phoneNumber;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Address> addresses;

    private Long mainAddressId;

    @ManyToMany(mappedBy = "users")
    private List<Role> roles;
}
