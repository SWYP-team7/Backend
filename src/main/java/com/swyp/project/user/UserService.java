package com.swyp.project.user;

import org.springframework.stereotype.Service;

import com.swyp.project.common.exception.UserNotFoundException;
import com.swyp.project.user.domain.User;
import com.swyp.project.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public UserResponse.ProfileStatus getProfileStatus(Long loggedInUserId) {
		User user = findUser(loggedInUserId);
		return new UserResponse.ProfileStatus(user.getProfileCompleted());
	}

	private User findUser(Long userId){
		return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}
}
