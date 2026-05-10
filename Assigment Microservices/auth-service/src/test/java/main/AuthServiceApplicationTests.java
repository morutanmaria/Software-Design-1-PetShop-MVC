package main;

import main.model.entity.Role;
import main.model.entity.User;
import main.model.repository.UserRepository;
import main.model.service.UserServiceClass;
import main.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceApplicationTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceClass userService;

	private JwtUtil jwtUtil;
	private BCryptPasswordEncoder encoder;

	@BeforeEach
	void setUp() {
		encoder = new BCryptPasswordEncoder();
		jwtUtil = new JwtUtil();
		ReflectionTestUtils.setField(jwtUtil, "secret", "petshop-secret-key-must-be-32-chars-min");
	}


	@Test
	void registerUser_shouldEncodePasswordAndSave() {
		User user = new User("john", "plainpassword", Role.SELLER, "john@test.com");
		when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

		User saved = userService.registerUser(user);

		assertNotNull(saved);
		assertNotEquals("plainpassword", saved.getPassword()); // password must be encoded
		assertTrue(encoder.matches("plainpassword", saved.getPassword()));
		verify(userRepository).save(any(User.class));
	}


	@Test
	void loginUser_withValidCredentials_shouldReturnUser() {
		String rawPassword = "secret";
		User stored = new User("john", encoder.encode(rawPassword), Role.SELLER, "john@test.com");
		when(userRepository.findByUsername("john")).thenReturn(Optional.of(stored));

		User result = userService.loginUser("john", rawPassword);

		assertNotNull(result);
		assertEquals("john", result.getUsername());
	}

	@Test
	void loginUser_withWrongPassword_shouldReturnNull() {
		User stored = new User("john", encoder.encode("secret"), Role.SELLER, "john@test.com");
		when(userRepository.findByUsername("john")).thenReturn(Optional.of(stored));

		User result = userService.loginUser("john", "wrongpassword");

		assertNull(result);
	}

	@Test
	void loginUser_withUnknownUsername_shouldReturnNull() {
		when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

		User result = userService.loginUser("unknown", "any");

		assertNull(result);
	}

	@Test
	void findUserByUsername_shouldReturnUserWhenFound() {
		User user = new User("john", "encoded", Role.ADMIN, "john@test.com");
		when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

		Optional<User> result = userService.findUserByUsername("john");

		assertTrue(result.isPresent());
		assertEquals("john", result.get().getUsername());
	}


	@Test
	void jwtUtil_shouldGenerateAndValidateToken() {
		String token = jwtUtil.generateToken("john", "ADMIN");

		assertNotNull(token);
		assertTrue(jwtUtil.isTokenValid(token));
		assertEquals("john", jwtUtil.extractUsername(token));
		assertEquals("ADMIN", jwtUtil.extractRole(token));
	}

	@Test
	void jwtUtil_invalidToken_shouldReturnFalse() {
		assertFalse(jwtUtil.isTokenValid("invalid.token.here"));
	}
}
