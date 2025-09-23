package com.curio.blog.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    AUTHOR;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
