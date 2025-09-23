package com.swyp.project.conversation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.ConversationCardSave;

public interface ConversationCardSaveRepository extends JpaRepository<ConversationCardSave, Long> {
	List<ConversationCardSave> findAllByUserId(Long userId);

	void deleteByConversationCardId(Long conversationCardId);
}
