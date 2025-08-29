package com.swyp.project.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.user.domain.UserProfileKeyword;

public interface UserProfileKeywordRepository extends JpaRepository<UserProfileKeyword, Long> {
	List<UserProfileKeyword> findByUserId(Long userId);

	void deleteByUserId(Long userId);
}
