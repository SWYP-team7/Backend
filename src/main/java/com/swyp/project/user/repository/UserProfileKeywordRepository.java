package com.swyp.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.user.domain.UserProfileKeyword;

public interface UserProfileKeywordRepository extends JpaRepository<UserProfileKeyword, Long> {
}
