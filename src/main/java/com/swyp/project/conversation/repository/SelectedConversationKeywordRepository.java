package com.swyp.project.conversation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.SelectedConversationKeyword;

public interface SelectedConversationKeywordRepository extends JpaRepository<SelectedConversationKeyword, Long> {

	List<SelectedConversationKeyword> findByConversationId(Long conversationId);
}
