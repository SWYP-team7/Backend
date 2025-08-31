package com.swyp.project.conversation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.ConversationCardSave;

public interface ConversationCardSaveRepository extends JpaRepository<ConversationCardSave, Long> {
}
