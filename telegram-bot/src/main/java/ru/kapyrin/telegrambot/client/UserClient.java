package ru.kapyrin.telegrambot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.kapyrin.telegrambot.dto.UserCreateDTO;
import ru.kapyrin.telegrambot.dto.UserDetailDTO;

@FeignClient(name = "userClient", url = "${user.service.url}")
public interface UserClient {

    @PostMapping("/api/users")
    UserDetailDTO createUser(@RequestBody UserCreateDTO dto);
}
