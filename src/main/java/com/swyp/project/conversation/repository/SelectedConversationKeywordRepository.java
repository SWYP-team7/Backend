package com.swyp.project.conversation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.SelectedConversationKeyword;

public interface SelectedConversationKeywordRepository extends JpaRepository<SelectedConversationKeyword, Long> {
}
