package com.example.ojt.security.principle;

import com.example.ojt.exception.NotFoundException;
import com.example.ojt.model.entity.Account;
import com.example.ojt.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsServiceCustom implements UserDetailsService {
    @Autowired
    private IAccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = null;
        try {
            account = accountRepository.findByEmail(email).orElseThrow(()->new NotFoundException("email not found"));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        return AccountDetailsCustom.build(account);
    }
}
