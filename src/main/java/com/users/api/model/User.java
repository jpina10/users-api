package com.users.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_address",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Address> addresses = new ArrayList<>();

    private Long mainAddressId;

    @ManyToMany(mappedBy = "users")
    private List<Role> roles;

    public void addAddress(Address address) {
        if(addresses.isEmpty()) {
            mainAddressId = address.getId();
        }

        addresses.add(address);
        address.getUsers().add(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.getUsers().remove(this);
    }
}
