package com.users.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    private Long id;

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Address address;
}
