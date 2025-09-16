package com.swyp.project.conversation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.ConversationRelationship;

public interface ConversationRelationshipRepository extends JpaRepository<ConversationRelationship, Long> {
}
