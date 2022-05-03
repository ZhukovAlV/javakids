package ru.javakids.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author avzhukov
 * @since 17.03.2022
 */
public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
