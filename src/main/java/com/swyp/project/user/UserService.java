package com.swyp.project.user;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.project.common.auth.UserContext;
import com.swyp.project.common.exception.ProfileKeywordNotFoundException;
import com.swyp.project.common.exception.UserNotFoundException;
import com.swyp.project.user.domain.ProfileKeyword;
import com.swyp.project.user.domain.User;
import com.swyp.project.user.domain.UserProfileKeyword;
import com.swyp.project.user.dto.UserRequest;
import com.swyp.project.user.dto.UserResponse;
import com.swyp.project.user.repository.ProfileKeywordRepository;
import com.swyp.project.user.repository.UserProfileKeywordRepository;
import com.swyp.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final ProfileKeywordRepository profileKeywordRepository;
	private final UserProfileKeywordRepository userProfileKeywordRepository;

	public UserResponse.ProfileStatus getProfileStatus() {
		Long loggedInUserId = UserContext.get().id();

		User user = findUser(loggedInUserId);
		return new UserResponse.ProfileStatus(user.getProfileCompleted());
	}

	@Transactional
	public void upsertProfile(UserRequest.UpsertProfile request) {
		Long loggedInUserId = UserContext.get().id();

		User user = findUser(loggedInUserId);

		if (request.name() != null && request.birthdate() != null && request.gender() != null) {
			user.updateProfile(request.name(), request.birthdate(), request.gender());
		}

		if (request.personalityKeywords() != null && request.conversationKeywords() != null
			&& request.interestKeywords() != null) {

			userProfileKeywordRepository.deleteAll();
			upsertProfileKeyword(request.personalityKeywords(), user);
			upsertProfileKeyword(request.conversationKeywords(), user);
			upsertProfileKeyword(request.interestKeywords(), user);
		}

		if (!user.getProfileCompleted()) {
			user.completeProfile();
		}
	}

	private void upsertProfileKeyword(List<String> keywords, User user) {
		keywords.stream()
			.map(keyword -> profileKeywordRepository.findByContent(keyword)
				.orElseThrow(ProfileKeywordNotFoundException::new))
			.map(keyword -> UserProfileKeyword.builder()
				.user(user)
				.profileKeyword(keyword)
				.build())
			.forEach(userProfileKeywordRepository::save);
	}

	@Transactional(readOnly = true)
	public UserResponse.ProfileKeywordByCategory getProfileKeyword() {

		Long loggedInUserId = UserContext.get().id();

		List<ProfileKeyword> keywords = profileKeywordRepository.findAll();

		// 유저 키워드 id 목록
		List<Long> selectedKeywordIds = userProfileKeywordRepository.findByUserId(loggedInUserId).stream()
			.map(userProfileKeyword -> userProfileKeyword.getProfileKeyword().getId())
			.toList();

		Map<String, List<ProfileKeyword>> keywordsByCategoryName = keywords.stream()
			.collect(Collectors.groupingBy(ProfileKeyword::getCategoryName));

		List<UserResponse.CategoryInfo> categoryInfos = keywordsByCategoryName.entrySet().stream()
			.map(entry -> {
				String categoryName = entry.getKey();
				List<UserResponse.ProfileKeywordInfo> keywordInfos = entry.getValue().stream()
					.map(keyword -> UserResponse.ProfileKeywordInfo.builder()
						.id(keyword.getId())
						.content(keyword.getContent())
						.isSelected(selectedKeywordIds.contains(keyword.getId()))
						.build())
					.collect(Collectors.toList());

				return UserResponse.CategoryInfo.builder()
					.name(categoryName)
					.keywords(keywordInfos)
					.build();
			})
			.collect(Collectors.toList());

		return new UserResponse.ProfileKeywordByCategory(categoryInfos);
	}

	public UserResponse.Summary getSummary() {
		Long loggedInUserId = UserContext.get().id();

		User user = findUser(loggedInUserId);
		return UserResponse.Summary.builder()
			.profileImageUrl(user.getProfileImageUrl())
			.name(user.getName())
			.email(user.getEmail())
			.build();
	}

	public UserResponse.Profile getProfile() {
		Long loggedInUserId = UserContext.get().id();

		User user = findUser(loggedInUserId);
		return UserResponse.Profile.builder()
			.profileImageUrl(user.getProfileImageUrl())
			.name(user.getName())
			.birthdate(user.getBirthdate())
			.gender(user.getGender())
			.build();
	}

	private User findUser(Long userId){
		return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}
}
