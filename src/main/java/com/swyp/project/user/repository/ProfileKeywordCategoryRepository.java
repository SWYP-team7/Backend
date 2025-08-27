package com.swyp.project.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.swyp.project.user.domain.ProfileKeywordCategory;

public interface ProfileKeywordCategoryRepository extends JpaRepository<ProfileKeywordCategory, Long> {

	List<ProfileKeywordCategory> findAllByOrderByDisplayOrderAsc();
}
