package com.swyp.project.conversation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.ConversationReport;

public interface ConversationReportRepository extends JpaRepository<ConversationReport, Long> {
	Optional<ConversationReport> findByConversationId(Long conversationId);

	Optional<ConversationReport> findByShareUuid(String shareUuid);
}
