package com.animalrescue.controller;

import com.animalrescue.service.AuthService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			Map<String, Object> response = authService.register(request.getUsername(), request.getEmail(),
					request.getPassword(), request.getFullName(), request.getPhoneNumber());
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			Map<String, Object> response = authService.login(request.getUsername(), request.getPassword());
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
		}
	}

	// Inner request classes
	public static class RegisterRequest {
		@NotBlank
		private String username;
		@Email
		@NotBlank
		private String email;
		@NotBlank
		private String password;
		private String fullName;
		private String phoneNumber;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
	}

	public static class LoginRequest {
		@NotBlank
		private String username;
		@NotBlank
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
