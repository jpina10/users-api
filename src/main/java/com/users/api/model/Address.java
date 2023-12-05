package com.users.api.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
