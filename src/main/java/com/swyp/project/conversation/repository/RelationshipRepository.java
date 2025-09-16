package com.swyp.project.conversation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.Relationship;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
	Optional<Relationship> findByContent(String content);
}
