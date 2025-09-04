package com.swyp.project.conversation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

	List<Participant> findByConversationId(Long conversationId);
}
