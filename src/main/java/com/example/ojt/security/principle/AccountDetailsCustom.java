package com.example.ojt.security.principle;

import com.example.ojt.model.entity.Account;
import com.example.ojt.model.entity.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountDetailsCustom implements UserDetails {
    private Integer id;
    private String email;
    private String password;
    private Integer status;
    private Role role;
    private Collection<? extends GrantedAuthority> authorities;

    public static AccountDetailsCustom build(Account account){
        Role role = account.getRole();
        GrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName().name());
        return AccountDetailsCustom.builder()
                .id(account.getId())
                .password(account.getPassword())
                .email(account.getEmail())
                .authorities(Collections.singletonList(authority))
                .status(account.getStatus())
                .role(role)
                .build();
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
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == 1;
    }
    public String getRoleName() {
        return role.getRoleName().name();
    }
}