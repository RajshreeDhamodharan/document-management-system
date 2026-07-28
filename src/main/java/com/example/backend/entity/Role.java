package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    SUPER_ADMIN,
    OWNER,
    EDITOR,
    VIEWER,
    APPROVER,
    GUEST;

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}