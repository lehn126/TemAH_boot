package com.temah.auth.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Service used to retrieving users to authenticate
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 仅用于测试的默认用户
        Collection<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("test,add,get,del");
        return User.withUsername("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("ADMIN").authorities(authorities).build();
    }
}
