package com.swyp.project.user.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", length = 50)
	private String name;

	@Column(name = "email", nullable = false, unique = true, length = 50)
	private String email;

	@Column(name = "birthdate")
	private LocalDate birthdate;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "social_id")
	private Long socialId;

	@Column(name = "provider", length = 20)
	private String provider;

	@Column(name = "provider_id", length = 100)
	private String providerId;

	@Column(name = "gender", length = 10)
	private String gender;

	@Column(name = "profile_image_url", length = 2048)
	private String profileImageUrl;

	@Column(name = "profile_completed")
	private Boolean profileCompleted;

	public void updateProfile(String name, LocalDate birthdate, String gender){
		this.name = name;
		this.birthdate = birthdate;
		this.gender = gender;
	}

	public void completeProfile() {
		this.profileCompleted = true;
	}
}
