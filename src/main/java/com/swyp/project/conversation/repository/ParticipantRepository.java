package com.swyp.project.conversation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.project.conversation.domain.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
