package com.swyp.project.conversation.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.project.ai.AiClient;
import com.swyp.project.ai.dto.AiRequest;
import com.swyp.project.ai.dto.AiResponse;
import com.swyp.project.common.auth.UserContext;
import com.swyp.project.common.exception.CategoryNotFound;
import com.swyp.project.common.exception.ConversationCardNotFound;
import com.swyp.project.common.exception.ConversationNotFound;
import com.swyp.project.common.exception.ConversationReportNotFound;
import com.swyp.project.common.exception.RelationshipNotFound;
import com.swyp.project.common.exception.UserNotFoundException;
import com.swyp.project.conversation.domain.Category;
import com.swyp.project.conversation.domain.Conversation;
import com.swyp.project.conversation.domain.ConversationCard;
import com.swyp.project.conversation.domain.ConversationCardSave;
import com.swyp.project.conversation.domain.ConversationKeyword;
import com.swyp.project.conversation.domain.ConversationRelationship;
import com.swyp.project.conversation.domain.ConversationReport;
import com.swyp.project.conversation.domain.Participant;
import com.swyp.project.conversation.domain.Relationship;
import com.swyp.project.conversation.domain.SelectedConversationKeyword;
import com.swyp.project.conversation.dto.ConversationFlatRow;
import com.swyp.project.conversation.dto.ConversationRequest;
import com.swyp.project.conversation.dto.ConversationResponse;
import com.swyp.project.conversation.repository.CategoryRepository;
import com.swyp.project.conversation.repository.ConversationCardRepository;
import com.swyp.project.conversation.repository.ConversationCardSaveRepository;
import com.swyp.project.conversation.repository.ConversationKeywordRepository;
import com.swyp.project.conversation.repository.ConversationRelationshipRepository;
import com.swyp.project.conversation.repository.ConversationReportRepository;
import com.swyp.project.conversation.repository.ConversationRepository;
import com.swyp.project.conversation.repository.ParticipantRepository;
import com.swyp.project.conversation.repository.RelationshipRepository;
import com.swyp.project.conversation.repository.SelectedConversationKeywordRepository;
import com.swyp.project.user.UserService;
import com.swyp.project.user.domain.User;
import com.swyp.project.user.dto.UserDto;
import com.swyp.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationService {
	public static final int TOTAL_QUESTION_COUNT = 20;
	private static final String BASE_SHARE_URL = "http://damdam.r-e.kr/reports/";

	private final UserService userService;
	private final AiClient aiClient;
	private final ConversationRepository conversationRepository;
	private final CategoryRepository categoryRepository;
	private final ParticipantRepository participantRepository;
	private final UserRepository userRepository;
	private final ConversationRelationshipRepository conversationRelationshipRepository;
	private final RelationshipRepository relationshipRepository;
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
			.category(category)
			.build();

		Conversation savedConversation = conversationRepository.save(conversation);

		// 관계 저장
		List<String> relationships = request.relationship().stream()
			.map(String::trim)
			.distinct()
			.toList();

		for (String relationship : relationships){
			Relationship foundRelationship = relationshipRepository.findByContent(relationship)
				.orElseThrow(RelationshipNotFound::new);

			ConversationRelationship conversationRelationship = ConversationRelationship.builder()
				.conversation(conversation)
				.relationship(foundRelationship)
				.build();

			conversationRelationshipRepository.save(conversationRelationship);
		}

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

	@Transactional(readOnly = true)
	public ConversationResponse.ReportAnalysis findReport(Long conversationId) {

		ConversationReport report = conversationReportRepository.findByConversationId(conversationId)
			.orElseThrow(ConversationReportNotFound::new);

		Conversation conversation = report.getConversation();
		List<String> conversationKeywordNames = selectedConversationKeywordRepository.findByConversationId(
			conversationId).stream().map(sck -> sck.getConversationKeyword().getContent()).toList();

		List<String> participantNames = participantRepository.findByConversationId(conversationId)
			.stream()
			.map(Participant::getName)
			.toList();

		return new ConversationResponse.ReportAnalysis(
			participantNames,
			conversation.getCategory().getContent(),
			conversationKeywordNames,
			conversation.getCreatedAt(),
			report.getDurationSeconds(),
			report.getNumQuestions(),
			report.getNumHearts(),
			report.getComment(),
			report.getNextRecommendedTopic(),
			report.getShareUuid(),
			BASE_SHARE_URL + report.getShareUuid()
		);
	}

	@Transactional
	public void saveGeneratedQuestions(AiResponse.GeneratedQuestions generatedQuestions, Long conversationId) {
		Conversation conversation = conversationRepository.findById(conversationId)
			.orElseThrow(ConversationNotFound::new);

		List<ConversationCard> conversationCards = new ArrayList<>();
		List<AiResponse.QuestionList> questionList = generatedQuestions.questionLists();

		for (int i = 0; i < questionList.size(); i++) {
			AiResponse.QuestionList questions = questionList.get(i);
			String keyword = String.join(", ", questions.keywords());
			for (int j = 0; j < questions.questions().size(); j++) {
				String content = questions.questions().get(j).text();
				ConversationCard card = ConversationCard.builder()
					.conversation(conversation)
					.content(content)
					.level(j + 1)
					.orderIndex(i + 1)
					.source("OPEN_AI")
					.cardKeyword(keyword)
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
			conversationId, request.orderIndex(), request.depth()).orElseThrow(ConversationCardNotFound::new);

		ConversationCardSave conversationCardSave = ConversationCardSave.builder()
			.user(user)
			.conversationCard(conversationCard)
			.build();

		conversationCardSaveRepository.save(conversationCardSave);
	}

	@Transactional
	public void unsaveCard(Long conversationId, ConversationRequest.ConversationCard request) {

		ConversationCard conversationCard = conversationCardRepository.findByConversationIdAndOrderIndexAndLevel(
			conversationId, request.orderIndex(), request.depth()).orElseThrow(ConversationCardNotFound::new);

		conversationCardSaveRepository.deleteByConversationCardId(conversationCard.getId());
	}

	@Transactional
	public ConversationResponse.End endConversation(Long conversationId, ConversationRequest.End request) {
		// 카드 저장
		User user = findUser();
		List<ConversationRequest.ConversationCard> savedConversationCards = request.savedConversationCards();

		for (ConversationRequest.ConversationCard cardRequest : savedConversationCards) {
			ConversationCard card = conversationCardRepository.findByConversationIdAndOrderIndexAndLevel(
				conversationId, cardRequest.orderIndex(), cardRequest.depth()).orElseThrow(ConversationCardNotFound::new);

			ConversationCardSave save = ConversationCardSave.builder()
				.user(user)
				.conversationCard(card)
				.build();

			conversationCardSaveRepository.save(save);
		}
		// 리포트 생성
		Conversation conversation = conversationRepository.findById(conversationId)
			.orElseThrow(ConversationNotFound::new);

		AiRequest.ReportInfo reportInfo = new AiRequest.ReportInfo(request.durationSeconds(), TOTAL_QUESTION_COUNT,
			request.numHearts(),
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

		conversation.end();

		conversationReportRepository.save(conversationReport);

		return new ConversationResponse.End(conversation.getId());
	}

	private User findUser() {
		return userRepository.findById(UserContext.get().id()).orElseThrow(UserNotFoundException::new);
	}

	public ConversationResponse.ReportAnalysisForShare getReportByShareUuid(String shareUuid) {
		ConversationReport report = conversationReportRepository.findByShareUuid(shareUuid)
			.orElseThrow(ConversationReportNotFound::new);

		Conversation conversation = report.getConversation();

		Long userId = conversation.getUser().getId();
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		List<String> participantNames = participantRepository.findByConversationId(conversation.getId())
			.stream()
			.map(p -> p.getName())
			.collect(Collectors.toList());

		List<String> keywords = selectedConversationKeywordRepository.findByConversationId(conversation.getId())
			.stream()
			.map(sck -> sck.getConversationKeyword().getContent())
			.toList();

		String participantNameStr = getParticipantNameStr(participantNames, user.getName());
		String duration = formatDuration(report.getDurationSeconds());

		return new ConversationResponse.ReportAnalysisForShare(
			conversation.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd H:mm")),
			participantNameStr,
			participantNames.size(),
			conversation.getCategory().getContent(),
			keywords,
			duration,
			20,
			report.getNumHearts(),
			report.getComment(),
			report.getNextRecommendedTopic()
		);
	}

	private String formatDuration(Integer seconds) {
		int mins = seconds / 60;
		int secs = seconds % 60;
		return String.format("%d분 %d초", mins, secs);
	}

	private String getParticipantNameStr(List<String> participantNames, String userName) {
		participantNames.remove(userName);

		if (participantNames.size() == 1) {
			return participantNames.getFirst();
		} else if (participantNames.size() > 1) {
			return participantNames.getFirst() + "...";
		}
		return "...";
	}

	@Transactional(readOnly = true)
	public ConversationResponse.Conversations findConversations() {
		User user = findUser();
		List<ConversationFlatRow> rows = conversationRepository.findConversationRow(user.getId());

		Map<Long, ConversationResponse.Summary> summaryMap = new LinkedHashMap<>();
		Map<Long, List<String>> participantMap = new HashMap<>();

		for (ConversationFlatRow row : rows) {
			participantMap
				.computeIfAbsent(row.getConversationId(), k -> new ArrayList<>())
				.add(row.getParticipantName());

			summaryMap.computeIfAbsent(
				row.getConversationId(),
				id -> new ConversationResponse.Summary(
					id,
					null,
					row.getCreatedAt(),
					row.getDurationSeconds(),
					row.getCategory().getContent()
				)
			);
		}

		for (var entry : summaryMap.entrySet()) {
			List<String> participants = participantMap.getOrDefault(entry.getKey(), List.of());
			String title = getParticipantNameStr(new ArrayList<>(participants), user.getName());

			ConversationResponse.Summary updated = new ConversationResponse.Summary(
				entry.getValue().conversationId(),
				title,
				entry.getValue().createdAt(),
				entry.getValue().durationSeconds(),
				entry.getValue().category()
			);
			entry.setValue(updated);
		}

		return new ConversationResponse.Conversations(new ArrayList<>(summaryMap.values()));
	}

	@Transactional(readOnly = true)
	public ConversationResponse.SavedCards findSavedCards() {
		User user = findUser();
		List<ConversationCardSave> savedCards = conversationCardSaveRepository.findAllByUserId(user.getId());

		List<ConversationResponse.SavedCardInfo> cardList = savedCards.stream()
			.map(card -> ConversationResponse.SavedCardInfo.from(card.getConversationCard()))
			.toList();

		return new ConversationResponse.SavedCards(cardList);
	}

	@Transactional
	public void deleteSavedCards(ConversationRequest.Delete request) {
		User user = findUser();
		List<ConversationCard> cards = new ArrayList<>();

		for (Long cardId : request.cardIds()) {
			ConversationCard card = conversationCardRepository.findById(cardId)
				.orElseThrow(ConversationCardNotFound::new);
			cards.add(card);
		}

		List<ConversationCardSave> saves = conversationCardSaveRepository.findAllByUserIdAndConversationCardIn(
			user.getId(), cards);

		conversationCardSaveRepository.deleteAll(saves);
	}
}
