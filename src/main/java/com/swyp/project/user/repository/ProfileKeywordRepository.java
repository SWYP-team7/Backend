package com.swyp.project.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.user.domain.ProfileKeyword;

public interface ProfileKeywordRepository extends JpaRepository<ProfileKeyword, Long> {
	Optional<ProfileKeyword> findByContent(String content);
}

