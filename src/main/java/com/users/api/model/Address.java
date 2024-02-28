package com.users.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String number;
    private String city;
    private String country;

    @Column(unique = true)
    private String postCode;

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();
}
