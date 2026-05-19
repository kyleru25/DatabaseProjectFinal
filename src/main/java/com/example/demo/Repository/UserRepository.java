package com.example.demo.Repository;

import com.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAddressIgnoreCase(String emailAddress);

    Optional<User> findByEmailAddressIgnoreCase(String emailAddress);
}