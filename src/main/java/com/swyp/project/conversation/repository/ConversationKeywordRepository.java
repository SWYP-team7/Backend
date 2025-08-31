package com.swyp.project.conversation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.ConversationKeyword;

public interface ConversationKeywordRepository extends JpaRepository<ConversationKeyword, Long> {
	Optional<ConversationKeyword> findByContent(String content);
}
