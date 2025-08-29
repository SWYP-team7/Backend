package com.swyp.project.conversation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByContent(String content);
}
