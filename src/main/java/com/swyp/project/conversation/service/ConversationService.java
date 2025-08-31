package com.swyp.project.conversation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.swyp.project.ai.AiClient;
import com.swyp.project.ai.dto.AiResponse;
import com.swyp.project.common.auth.UserContext;
import com.swyp.project.common.exception.CategoryNotFound;
import com.swyp.project.common.exception.ConversationCardNotFound;
import com.swyp.project.common.exception.ConversationNotFound;
import com.swyp.project.common.exception.UserNotFoundException;
import com.swyp.project.conversation.domain.Category;
import com.swyp.project.conversation.domain.Conversation;
import com.swyp.project.conversation.domain.ConversationCard;
import com.swyp.project.conversation.domain.ConversationCardSave;
import com.swyp.project.conversation.domain.Participant;
import com.swyp.project.conversation.dto.ConversationRequest;
import com.swyp.project.conversation.repository.CategoryRepository;
import com.swyp.project.conversation.repository.ConversationCardRepository;
import com.swyp.project.conversation.repository.ConversationCardSaveRepository;
import com.swyp.project.conversation.repository.ConversationRepository;
import com.swyp.project.conversation.repository.ParticipantRepository;
import com.swyp.project.user.UserService;
import com.swyp.project.user.domain.User;
import com.swyp.project.user.dto.UserDto;
import com.swyp.project.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationService {
	private final UserService userService;
	private final AiClient aiClient;
	private final ConversationRepository conversationRepository;
	private final CategoryRepository categoryRepository;
	private final ParticipantRepository participantRepository;
	private final UserRepository userRepository;
	private final ConversationCardRepository conversationCardRepository;
	private final ConversationCardSaveRepository conversationCardSaveRepository;

	@Transactional
	public Long createConversation(ConversationRequest.Create request) {
		// todo: 키워드 내용은 없기 때문에 추가해야함
		User user = findUser();

		Category category = categoryRepository.findByContent(request.category().trim())
			.orElseThrow(CategoryNotFound::new);

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

		return savedConversation.getId();
	}

	public AiResponse.GeneratedQuestions generateQuestions(ConversationRequest.Create request) {
		UserDto.Info userInfo = userService.getUserInfo();
		return aiClient.generateQuestions(request, userInfo);
	}

	@Transactional
	public void saveGeneratedQuestions(AiResponse.GeneratedQuestions generatedQuestions, Long conversationId) {
		Conversation conversation = conversationRepository.findById(conversationId)
			.orElseThrow(ConversationNotFound::new);

		List<ConversationCard> conversationCards = new ArrayList<>();
		List<AiResponse.QuestionList> questionList = generatedQuestions.questionLists();

		for (int i = 0; i < questionList.size(); i++) {
			AiResponse.QuestionList questions = questionList.get(i);
			for (int j = 0; j < questions.questions().size(); j++) {
				String content = questions.questions().get(j).text();
				ConversationCard card = ConversationCard.builder()
					.conversation(conversation)
					.content(content)
					.level(j + 1)
					.orderIndex(i + 1)
					.source("OPEN_AI")
					.build();
				conversationCards.add(card);
			}
		}

		conversationCardRepository.saveAll(conversationCards);
	}

	@Transactional
	public void saveConversationCard(Long conversationId, ConversationRequest.ConversationCard request) {
		User user = findUser();
		ConversationCard conversationCard = conversationCardRepository.findByConversationIdAndOrderIndexAndDepth(
			conversationId, request.orderIndex(),
			request.depth()).orElseThrow(ConversationCardNotFound::new);

		ConversationCardSave conversationCardSave = ConversationCardSave.builder()
			.user(user)
			.conversationCard(conversationCard)
			.build();

		conversationCardSaveRepository.save(conversationCardSave);
	}

	private User findUser() {
		return userRepository.findById(UserContext.get().id()).orElseThrow(UserNotFoundException::new);
	}
}
