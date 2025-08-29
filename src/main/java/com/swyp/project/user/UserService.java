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
import com.swyp.project.user.dto.ProfileKeywordResponse;
import com.swyp.project.user.dto.UserRequest;
import com.swyp.project.user.dto.UserResponse;
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

	public UserResponse.ProfileStatus getProfileStatus() {
		Long loggedInUserId = UserContext.get().id();

		User user = findUser(loggedInUserId);
		return new UserResponse.ProfileStatus(user.getProfileCompleted());
	}

	@Transactional
	public void upsertProfile(UserRequest.UpsertProfile request) {
		Long loggedInUserId = UserContext.get().id();

		User user = findUser(loggedInUserId);

		user.updateProfile(request.name(), request.birthdate(), request.gender());

		if (request.keywordIds() != null) {
			userProfileKeywordRepository.deleteByUserId(user.getId());
			request.keywordIds().stream()
				.map(keywordId -> profileKeywordRepository.findById(keywordId)
					.orElseThrow(ProfileKeywordNotFoundException::new))
				.map(keyword -> UserProfileKeyword.builder()
					.user(user)
					.profileKeyword(keyword)
					.build())
				.forEach(userProfileKeywordRepository::save);
		}

		if (!user.getProfileCompleted()) {
			user.completeProfile();
		}
	}

	@Transactional(readOnly = true)
	public ProfileKeywordResponse.ProfileKeywordByCategory getProfileKeyword() {

		Long loggedInUserId = UserContext.get().id();

		List<ProfileKeyword> keywords = profileKeywordRepository.findAll();

		// 유저 키워드 id 목록
		List<Long> selectedKeywordIds = userProfileKeywordRepository.findByUserId(loggedInUserId).stream()
			.map(userProfileKeyword -> userProfileKeyword.getProfileKeyword().getId())
			.toList();

		Map<String, List<ProfileKeyword>> keywordsByCategoryName = keywords.stream()
			.collect(Collectors.groupingBy(ProfileKeyword::getCategoryName));

		List<ProfileKeywordResponse.CategoryInfo> categoryInfos = keywordsByCategoryName.entrySet().stream()
			.map(entry -> {
				String categoryName = entry.getKey();
				List<ProfileKeywordResponse.ProfileKeywordInfo> keywordInfos = entry.getValue().stream()
					.map(keyword -> ProfileKeywordResponse.ProfileKeywordInfo.builder()
						.id(keyword.getId())
						.content(keyword.getContent())
						.isSelected(selectedKeywordIds.contains(keyword.getId()))
						.build())
					.collect(Collectors.toList());

				return ProfileKeywordResponse.CategoryInfo.builder()
					.name(categoryName)
					.keywords(keywordInfos)
					.build();
			})
			.collect(Collectors.toList());

		return new ProfileKeywordResponse.ProfileKeywordByCategory(categoryInfos);
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
