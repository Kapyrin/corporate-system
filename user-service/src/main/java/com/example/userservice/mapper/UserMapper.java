package com.example.userservice.mapper;

import com.example.userservice.dto.*;
import com.example.userservice.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserCreateDTO dto);

    UserDetailDTO toDetailDto(User user);

    UserSummaryDTO toSummaryDto(User user);

    List<UserSummaryDTO> toSummaryDtoList(List<User> users);
}
