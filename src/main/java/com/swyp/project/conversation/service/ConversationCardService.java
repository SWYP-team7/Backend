package com.swyp.project.conversation.service;

import org.springframework.stereotype.Service;

import com.swyp.project.conversation.repository.ConversationCardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationCardService {

	private final ConversationCardRepository conversationCardRepository;
}
