package com.example.userservice.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String roleName) {
        super("Role with name '" + roleName + "' was not found");
    }
}
