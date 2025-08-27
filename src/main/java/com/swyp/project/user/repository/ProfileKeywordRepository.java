package com.swyp.project.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.user.domain.ProfileKeyword;
import com.swyp.project.user.domain.ProfileKeywordCategory;

public interface ProfileKeywordRepository extends JpaRepository<ProfileKeyword, Long> {
	List<ProfileKeyword> findByCategoryIn(List<ProfileKeywordCategory> categories);
}
