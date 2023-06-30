package com.pack.v2.services;

import com.pack.v2.models.User;
import com.pack.v2.repositories.UserRepository;
import com.pack.v2.security.UserDet;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsSer implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsSer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            // Если пользователь не найден по имени пользователя, попробуйте найти его по адресу электронной почты
            user = userRepository.findByEmail(username);
        }

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new UserDet(user.get());
    }

}
