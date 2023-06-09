package com.pack.v2.repositories;

import com.pack.v2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailOrUsername(String email, String username);
}
