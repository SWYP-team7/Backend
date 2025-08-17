package com.swyp.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swyp.project.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
