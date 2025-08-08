package com.swyp.project.user.dto.request;

import java.time.LocalDateTime;

public record UpdateUserRequest(String name, LocalDateTime birthDate, String gender) {
}
