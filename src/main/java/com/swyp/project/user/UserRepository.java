package com.swyp.project.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
