package com.swyp.project.conversation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.ConversationCard;

public interface ConversationCardRepository extends JpaRepository<ConversationCard, Long> {
}
