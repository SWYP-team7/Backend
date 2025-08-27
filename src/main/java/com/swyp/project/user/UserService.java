package com.swyp.project.user;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.project.common.exception.ProfileAlreadyExistsException;
import com.swyp.project.common.exception.ProfileKeywordNotFoundException;
import com.swyp.project.common.exception.UserNotFoundException;
import com.swyp.project.user.domain.ProfileKeyword;
import com.swyp.project.user.domain.ProfileKeywordCategory;
import com.swyp.project.user.domain.User;
import com.swyp.project.user.domain.UserProfileKeyword;
import com.swyp.project.user.dto.ProfileKeywordResponse;
import com.swyp.project.user.dto.UserRequest;
import com.swyp.project.user.dto.UserResponse;
import com.swyp.project.user.repository.ProfileKeywordCategoryRepository;
import com.swyp.project.user.repository.ProfileKeywordRepository;
import com.swyp.project.user.repository.UserProfileKeywordRepository;
import com.swyp.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final ProfileKeywordRepository profileKeywordRepository;
	private final UserProfileKeywordRepository userProfileKeywordRepository;
	private final ProfileKeywordCategoryRepository profileKeywordCategoryRepository;

	public UserResponse.ProfileStatus getProfileStatus(Long loggedInUserId) {
		User user = findUser(loggedInUserId);
		return new UserResponse.ProfileStatus(user.getProfileCompleted());
	}

	@Transactional
	public void createProfile(UserRequest.CreateProfile request, Long loggedInUserId) {
		User user = findUser(loggedInUserId);

		// 프로필을 이미 작성했으면 예외 발생
		if(user.getProfileCompleted()) throw new ProfileAlreadyExistsException();
		user.updateProfile(request.name(), request.birthdate(), request.gender());

		request.keywordIds().stream()
			.map(keywordId -> profileKeywordRepository.findById(keywordId)
				.orElseThrow(ProfileKeywordNotFoundException::new))
			.map(keyword -> UserProfileKeyword.builder()
				.user(user)
				.profileKeyword(keyword)
				.build())
			.forEach(userProfileKeywordRepository::save);

		user.completeProfile();
	}

	@Transactional(readOnly = true)
	public ProfileKeywordResponse.ProfileKeywordByCategory findProfileKeyword() {
		List<ProfileKeywordCategory> categories = profileKeywordCategoryRepository.findAllByOrderByDisplayOrderAsc();

		List<ProfileKeyword> keywords = profileKeywordRepository.findByCategoryIn(categories);

		Map<Long, List<ProfileKeyword>> keywordsByCategoryId = keywords.stream()
			.collect(Collectors.groupingBy(keyword -> keyword.getCategory().getId()));

		List<ProfileKeywordResponse.CategoryInfo> categoryInfos = categories.stream()
			.map(category -> {
				List<ProfileKeyword> keywordsForCategory = keywordsByCategoryId.getOrDefault(category.getId(),
					Collections.emptyList());

				List<ProfileKeywordResponse.ProfileKeywordInfo> keywordInfos = keywordsForCategory.stream()
					.map(keyword -> ProfileKeywordResponse.ProfileKeywordInfo.builder()
						.id(keyword.getId())
						.content(keyword.getContent())
						.build())
					.collect(Collectors.toList());

				return ProfileKeywordResponse.CategoryInfo.builder()
					.id(category.getId())
					.name(category.getName())
					.displayOrder(category.getDisplayOrder())
					.keywords(keywordInfos)
					.build();
			})
			.collect(Collectors.toList());

		return new ProfileKeywordResponse.ProfileKeywordByCategory(categoryInfos);
	}


	public UserResponse.Summary getSummary(Long loggedInUserId) {
		User user = findUser(loggedInUserId);
		return UserResponse.Summary.builder()
			.profileImageUrl(user.getProfileImageUrl())
			.name(user.getName())
			.email(user.getEmail())
			.build();
	}

	public UserResponse.Profile getProfile(Long loggedInUserId) {
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
