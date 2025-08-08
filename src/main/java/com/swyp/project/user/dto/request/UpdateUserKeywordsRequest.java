package com.swyp.project.user.dto.request;

import java.util.List;

public record UpdateUserKeywordsRequest(List<Integer> keywordIds) {
}
