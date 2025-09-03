package com.swyp.project.conversation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.ConversationReport;

public interface ConversationReportRepository extends JpaRepository<ConversationReport, Long> {

}
