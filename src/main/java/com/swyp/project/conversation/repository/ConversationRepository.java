package com.swyp.project.conversation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.swyp.project.conversation.domain.Conversation;
import com.swyp.project.conversation.dto.ConversationFlatRow;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

	@Query("""
			select new com.swyp.project.conversation.dto.ConversationFlatRow(
				c.id,
				c.createdAt,
				c.category,
				p.name,
				r.durationSeconds
			)
			from Conversation c
			left join Participant p on p.conversation.id = c.id
			left join ConversationReport r on r.conversation.id = c.id
			where c.user.id = :userId
				and c.endedAt is not null
		""")
	List<ConversationFlatRow> findConversationRow(@Param("userId") Long userId);
}
