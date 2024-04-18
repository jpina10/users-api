package com.users.api.dto;

import com.users.api.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserCriteriaSpecification {

    private UserCriteriaSpecification() {
    }

    //use of like to be able to do partial search
    //use of lower to do ignore case
    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<User> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> hasUsername(String username) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }
}
