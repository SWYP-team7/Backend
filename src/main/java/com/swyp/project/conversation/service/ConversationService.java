package com.swyp.project.conversation.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.swyp.project.ai.AiClient;
import com.swyp.project.ai.dto.AiResponse;
import com.swyp.project.common.auth.UserContext;
import com.swyp.project.common.exception.CategoryNotFound;
import com.swyp.project.common.exception.UserNotFoundException;
import com.swyp.project.conversation.domain.Category;
import com.swyp.project.conversation.domain.Conversation;
import com.swyp.project.conversation.domain.Participant;
import com.swyp.project.conversation.dto.ConversationRequest;
import com.swyp.project.conversation.dto.ConversationResponse;
import com.swyp.project.conversation.repository.CategoryRepository;
import com.swyp.project.conversation.repository.ConversationRepository;
import com.swyp.project.conversation.repository.ParticipantRepository;
import com.swyp.project.user.domain.User;
import com.swyp.project.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationService {
	private final AiClient aiClient;
	private final ConversationRepository conversationRepository;
	private final CategoryRepository categoryRepository;
	private final ParticipantRepository participantRepository;
	private final UserRepository userRepository;

	@Transactional
	public ConversationResponse.Create createConversation(ConversationRequest.Create request){
		// todo: 키워드 내용은 없기 때문에 추가해야함
		User user = findUser();

		Category category = categoryRepository.findByContent(request.category().trim()).orElseThrow(CategoryNotFound::new);

		Conversation conversation = Conversation.builder()
			.user(user)
			.relationship(request.relationship().trim())
			.category(category)
			.build();

		Conversation savedConversation = conversationRepository.save(conversation);

		// participant 저장
		List<String> participantNames = request.participantNames().stream()
			.map(String::trim)
			.distinct()
			.toList();

		List<Participant> entities = participantNames.stream()
		       .map(n -> Participant.builder().name(n).conversation(savedConversation).build())
		       .toList();

		participantRepository.saveAll(entities);

		String uuid = UUID.randomUUID().toString();

		return new ConversationResponse.Create(
			savedConversation.getId(),
			uuid,
			"/api/conversations/" + uuid + "/subscribe"
		);
	}

	public AiResponse.GeneratedQuestions generateQuestions(ConversationRequest.Create request) {
		return aiClient.generateQuestions(request, null);
	}

	private User findUser() {
		return userRepository.findById(UserContext.get().id()).orElseThrow(UserNotFoundException::new);
	}
}
