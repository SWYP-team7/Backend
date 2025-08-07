package com.swyp.project.user.dto;

import java.util.List;

public record UpdateUserKeywordsRequest(List<Integer> keywordIds) {
}
