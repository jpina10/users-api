package com.users.api.dto;

import com.users.api.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserCriteriaSpecification {

    private UserCriteriaSpecification() {
    }

    //use of like to be able to do partial search
    //use of lower to do ignore case
    public static Specification<User> addField(String field, String value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }
}
