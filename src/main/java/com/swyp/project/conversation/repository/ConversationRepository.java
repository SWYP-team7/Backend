package com.swyp.project.conversation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
