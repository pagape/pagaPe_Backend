package com.edu.pe.pagaPeBackend.user.model;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    // Getter para el userId

    private Long id; // Atributo adicional (userId)
    private String username; // Normalmente el email o el nombre de usuario
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Cambiar según tu lógica
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Cambiar según tu lógica
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Cambiar según tu lógica
    }

    @Override
    public boolean isEnabled() {
        return true; // Cambiar según tu lógica
    }
}
