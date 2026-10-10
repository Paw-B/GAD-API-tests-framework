package tests;

import static api_adapters.UsersAdapter.*;
import static dto.NewUserDataFactory.createNewUserData;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.*;

import api_adapters.UsersAdapter;
import dto.NewUserData;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.users.*;
import org.testng.annotations.Test;

@Test(testName = "Users API Tests")
public class UsersTest extends BaseTest {

	@Feature("Users")
	@Severity(SeverityLevel.CRITICAL)
	@Test(testName = "Verify GET /users against schema")
	public void verifyGetUsersResponseAgainstSchema() {
		Response getUsersResponse = UsersAdapter.getUsers();

		assertTrue(
				matchesJsonSchemaInClasspath("json_schemas/get_users_schema.json")
						.matches(getUsersResponse.getBody().asString()),
				"Response for GET /users does not match the schema.");
	}

	@Feature("Users")
	@Severity(SeverityLevel.CRITICAL)
	@Test(testName = "Verify POST /users creates new user", priority = 1)
	public void verifyPostUserRequest() {
		NewUserData requestBody = createNewUserData();
		PostCreateUserRs createUserRequest = postCreateUser(requestBody);

		assertTrue(createUserRequest.id > 0, "Id was not assigned.");
		assertEquals(createUserRequest.email, requestBody.getEmail(), "Incorrect email returned.");
		assertEquals(createUserRequest.firstName, requestBody.getFirstName(), "Incorrect first name returned.");
		assertEquals(createUserRequest.lastName, requestBody.getLastName(), "Incorrect last name returned.");
		assertEquals(createUserRequest.password, requestBody.getPassword(), "Incorrect password returned.");
		assertEquals(createUserRequest.avatar, requestBody.getAvatar(), "Incorrect avatar returned.");
	}

	@Feature("Users")
	@Severity(SeverityLevel.NORMAL)
	@Test(testName = "Verify GET /users/{id} returns user", priority = 2)
	public void verifyGetUserRequest() {
		NewUserData requestBody = createNewUserData();
		PostCreateUserRs createUserRequest = postCreateUser(requestBody);
		int userId = createUserRequest.id;

		PostCreateUserRs getUser = UsersAdapter.getUser(userId);
		assertEquals(getUser.id, userId, "Incorrect user id returned.");
		assertEquals(getUser.firstName, requestBody.getFirstName(), "Incorrect first name returned.");
	}

	@Feature("Users")
	@Severity(SeverityLevel.CRITICAL)
	@Test(testName = "Verify PATCH /users/{id} changes user's data", priority = 3)
	public void verifyPatchUserRequest() {
		NewUserData requestBody = createNewUserData();
		PostCreateUserRs createUserRequest = postCreateUser(requestBody);
		int userId = createUserRequest.id;
		NewUserData requestBodyValuesForNewUser = createNewUserData();
		patchUser(requestBodyValuesForNewUser, requestBody, userId);
		PostCreateUserRs getUpdatedUserResponse = UsersAdapter.getUser(userId);

		assertEquals(getUpdatedUserResponse.firstName, requestBodyValuesForNewUser.getFirstName(),
				"First name was not patched");
		assertEquals(getUpdatedUserResponse.avatar, requestBodyValuesForNewUser.getAvatar(), "Avatar was not patched");
	}

	@Feature("Users")
	@Severity(SeverityLevel.CRITICAL)
	@Test(testName = "Verify DELETE /users/{id} deletes user", priority = 4)
	public void verifyDeleteUserRequest() {
		NewUserData requestBody = createNewUserData();
		PostCreateUserRs createUserRequest = postCreateUser(requestBody);
		int userId = createUserRequest.id;
		String authToken = getAccessToken(requestBody);
		int deleteUserResponseCode = deleteUser(userId, authToken);

		assertEquals(deleteUserResponseCode, 200, "Delete user failed");
		assertEquals(getDeletedUser(userId), 404, "Delete user failed");
	}
}