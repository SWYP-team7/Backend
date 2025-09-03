package com.swyp.project.conversation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.swyp.project.ai.AiClient;
import com.swyp.project.ai.dto.AiRequest;
import com.swyp.project.ai.dto.AiResponse;
import com.swyp.project.common.auth.UserContext;
import com.swyp.project.common.exception.CategoryNotFound;
import com.swyp.project.common.exception.ConversationCardNotFound;
import com.swyp.project.common.exception.ConversationKeywordNotFound;
import com.swyp.project.common.exception.ConversationNotFound;
import com.swyp.project.common.exception.UserNotFoundException;
import com.swyp.project.conversation.domain.Category;
import com.swyp.project.conversation.domain.Conversation;
import com.swyp.project.conversation.domain.ConversationCard;
import com.swyp.project.conversation.domain.ConversationCardSave;
import com.swyp.project.conversation.domain.ConversationKeyword;
import com.swyp.project.conversation.domain.ConversationReport;
import com.swyp.project.conversation.domain.Participant;
import com.swyp.project.conversation.domain.SelectedConversationKeyword;
import com.swyp.project.conversation.dto.ConversationRequest;
import com.swyp.project.conversation.dto.ConversationResponse;
import com.swyp.project.conversation.repository.CategoryRepository;
import com.swyp.project.conversation.repository.ConversationCardRepository;
import com.swyp.project.conversation.repository.ConversationCardSaveRepository;
import com.swyp.project.conversation.repository.ConversationKeywordRepository;
import com.swyp.project.conversation.repository.ConversationReportRepository;
import com.swyp.project.conversation.repository.ConversationRepository;
import com.swyp.project.conversation.repository.ParticipantRepository;
import com.swyp.project.conversation.repository.SelectedConversationKeywordRepository;
import com.swyp.project.user.UserService;
import com.swyp.project.user.domain.User;
import com.swyp.project.user.dto.UserDto;
import com.swyp.project.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationService {
	public static final int TOTAL_QUESTION_COUNT = 20;
	private final UserService userService;
	private final AiClient aiClient;
	private final ConversationRepository conversationRepository;
	private final CategoryRepository categoryRepository;
	private final ParticipantRepository participantRepository;
	private final UserRepository userRepository;
	private final ConversationKeywordRepository conversationKeywordRepository;
	private final SelectedConversationKeywordRepository selectedConversationKeywordRepository;
	private final ConversationCardRepository conversationCardRepository;
	private final ConversationCardSaveRepository conversationCardSaveRepository;
	private final ConversationReportRepository conversationReportRepository;

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

		// 키워드 저장
		List<String> keywords = request.keywords().stream()
			.map(String::trim)
			.distinct()
			.toList();

		for (String keyword : keywords) {
			ConversationKeyword conversationKeyword = conversationKeywordRepository.findByContent(keyword)
				.orElseGet(() -> conversationKeywordRepository.save(
					ConversationKeyword.builder()
						.content(keyword)
						.isPredefined(false)
						.build()
				));

			SelectedConversationKeyword selectedConversationKeyword = SelectedConversationKeyword.builder()
				.conversation(savedConversation)
				.conversationKeyword(conversationKeyword)
				.build();

			selectedConversationKeywordRepository.save(selectedConversationKeyword);
		}

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

	public ConversationResponse.ReportAnalysis findReport(Long conversationId) {

		ConversationReport report = conversationReportRepository.findByConversationId(conversationId)
			.orElseThrow(ConversationNotFound::new);



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
		ConversationCard conversationCard = conversationCardRepository.findByConversationIdAndOrderIndexAndLevel(
			conversationId, request.orderIndex(),
			request.depth()).orElseThrow(ConversationCardNotFound::new);

		ConversationCardSave conversationCardSave = ConversationCardSave.builder()
			.user(user)
			.conversationCard(conversationCard)
			.build();

		conversationCardSaveRepository.save(conversationCardSave);
	}

	@Transactional
	public ConversationResponse.End endConversation(Long conversationId, ConversationRequest.End request) {
		Conversation conversation = conversationRepository.findById(conversationId)
			.orElseThrow(ConversationNotFound::new);

		AiRequest.ReportInfo reportInfo = new AiRequest.ReportInfo(request.durationSeconds(), TOTAL_QUESTION_COUNT, request.numHearts(),
			conversation.getCategory().getContent());

		AiResponse.GeneratedReport generatedReport = aiClient.generateReport(reportInfo);

		ConversationReport conversationReport = ConversationReport.builder()
			.conversation(conversation)
			.durationSeconds(request.durationSeconds())
			.numQuestions(TOTAL_QUESTION_COUNT)
			.numHearts(request.numHearts())
			.comment(generatedReport.comment())
			.nextRecommendedTopic(generatedReport.nextTopic())
			.shareUuid(UUID.randomUUID().toString())
			.build();

		conversationReportRepository.save(conversationReport);

		return new ConversationResponse.End(conversation.getId());
	}

	private User findUser() {
		return userRepository.findBySocialId(UserContext.get().socialId()).orElseThrow(UserNotFoundException::new);
	}
}
