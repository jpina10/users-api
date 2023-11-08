package com.users.api.nameapi.api;

import com.users.api.nameapi.RandomUserApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "randomUserApiClient", url = "https://randomuser.me/api")
public interface RandomUserApiClient {

    @GetMapping
    RandomUserApiResponse getUserData();
}
