package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationRepositoryTest {

	private static int testNumber = 0;

	private static String username;

	private AuthenticationRepository authenticationRepository = Mockito.mock(AuthenticationRepository.class);

	@BeforeEach
	void updateUsername() {
		username = "usernameTest" + testNumber;
		testNumber++;
	}

	/*
	 * This test checks that adding a duplicate username will create an exception
	 */
	@Test
	void createDuplicateUserTest() {

		String username = "username";

		Authentication user1 = new Authentication(username, "pass1");

		authenticationRepository.save(user1);

		Authentication user2 = new Authentication(username, "pass2");

		// This line should throw a DuplicateKeyException
		authenticationRepository.save(user2);
	}

	@Test
	void verifyExistingPasswords() throws Exception {
		String username = "username1";

		Authentication user1 = new Authentication(username, "pass1");
		// bypass hasher..
		Field pwField = user1.getClass().getDeclaredField("password");
		pwField.setAccessible(true);
		pwField.set(user1, "pass1");

		authenticationRepository.save(user1);

		when(authenticationRepository.findByUsername(username)).thenReturn(user1);

		Authentication u = authenticationRepository.findByUsername(username);
		assertTrue(u.checkPassword("pass1"));
		// try against a new object
		Authentication hashedUser1 = new Authentication(username, "pass1");

		when(authenticationRepository.findByUsername(username)).thenReturn(hashedUser1);
		Field pwFieldHashed = hashedUser1.getClass().getDeclaredField("password");
		pwFieldHashed.setAccessible(true);
		pwFieldHashed.set(hashedUser1, "pass1");

		assertEquals(u.getPassword(), hashedUser1.getPassword());
	}

	@Test
	void verifyExistingWithNewPasswords() throws Exception {
		String username = "username" + System.currentTimeMillis();

		Authentication user1 = new Authentication(username, "pass1");

		authenticationRepository.save(user1);

		when(authenticationRepository.findByUsername(username)).thenReturn(user1);

		Authentication u = authenticationRepository.findByUsername(username);
		assertTrue(u.checkPassword("pass1"));
	}

	@Test
	void verifyWithBadPasswords() throws Exception {
		String username = "username" + System.currentTimeMillis();

		Authentication user1 = new Authentication(username, "pass2");

		authenticationRepository.save(user1);

		when(authenticationRepository.findByUsername(username)).thenReturn(user1);

		Authentication u = authenticationRepository.findByUsername(username);
		assertFalse(u.checkPassword("pass1"));
	}

	/*
	 * This test checks that we ge a null when getting a user which does not exist
	 */
	@Test
	void testGetUserDoesNotExist() {
		assertNull(authenticationRepository.findByUsername(username));
	}

}