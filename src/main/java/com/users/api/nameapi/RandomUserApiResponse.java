package com.users.api.nameapi;

import com.users.api.nameapi.model.Error;
import com.users.api.nameapi.model.Result;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RandomUserApiResponse {
    private List<Result> results;
    private Error error;
}
