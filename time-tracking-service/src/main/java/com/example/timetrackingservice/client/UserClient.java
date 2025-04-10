package com.example.timetrackingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "userClient", url = "${user-service.url}")
public interface UserClient {

    @GetMapping("/api/users/{id}/exists")
    Boolean userExists(@PathVariable("id") Long id);
}