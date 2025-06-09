package com.example.application.security;

import com.example.application.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public CustomUserDetailsService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        UserDetails admin = User.builder()
                .username("cyberanalyst")
                .password(passwordEncoder.encode("cisco"))
                .roles("ADMIN")
                .build();

        this.inMemoryUserDetailsManager = new InMemoryUserDetailsManager(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔑 Login attempt: " + username);
        com.example.application.data.User dbUser = userRepository.findByUsername(username);

        if (dbUser != null) {
            return User.builder()
                    .username(dbUser.getUsername())
                    .password(dbUser.getPassword())
                    .roles()
                    .build();
        }

        return inMemoryUserDetailsManager.loadUserByUsername(username);
    }

}
