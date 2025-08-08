package com.swyp.project.user.dto.request;

import java.time.LocalDate;

public record UpdateUserRequest(String name, LocalDate birthDate, String gender) {
}
