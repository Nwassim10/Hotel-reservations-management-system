package edu.miu.cs.cs544.service.impl;

import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.RoleType;
import edu.miu.cs.cs544.domain.User;
import edu.miu.cs.cs544.repository.CustomerRepository;
import edu.miu.cs.cs544.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No User Found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getUserPass(),
                user.getActive(),
                true,
                true,
                true,
                getAuthorities(List.of(user.getRoleType()))
        );
    }

        private Collection<? extends GrantedAuthority> getAuthorities(List<RoleType> roles) {
            List<GrantedAuthority>  authorities = new ArrayList<>();
            for(RoleType role: roles) {
                authorities.add(new SimpleGrantedAuthority(role.name()));
            }
            return authorities;
        }

}

