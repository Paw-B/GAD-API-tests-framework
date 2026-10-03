package tests;

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
		Response getUsersResponse = UsersAdapter.getUsersRequest();

		assertTrue(
				matchesJsonSchemaInClasspath("json_schemas/get_users_schema.json")
						.matches(getUsersResponse.getBody().asString()),
				"Response for GET /users does not match the schema.");
	}

	@Feature("Users")
	@Severity(SeverityLevel.CRITICAL)
	@Test(testName = "Verify POST /users creates new user", priority = 1)
	public void createNewUser() {
		NewUserData requestBodyValues = createNewUserData();
		PostUsersRs createNewUserRs = postCreateNewUser(requestBodyValues);

		assertTrue(createNewUserRs.id > 0, "Id was not assigned.");
		assertEquals(createNewUserRs.email, requestBodyValues.getEmail(), "Incorrect email returned.");
		assertEquals(createNewUserRs.firstname, requestBodyValues.getFirstname(), "Incorrect first name returned.");
		assertEquals(createNewUserRs.lastname, requestBodyValues.getLastname(), "Incorrect last name returned.");
		assertEquals(createNewUserRs.password, requestBodyValues.getPassword(), "Incorrect password returned.");
		assertEquals(createNewUserRs.avatar, requestBodyValues.getAvatar(), "Incorrect avatar returned.");
	}

	@Feature("Users")
	@Severity(SeverityLevel.NORMAL)
	@Test(testName = "Verify GET /users/{id} returns user", priority = 3)
	public void getUser() {
		NewUserData requestBodyValues = createNewUserData();
		PostUsersRs createNewUserRs = postCreateNewUser(requestBodyValues);
		int idOfCreatedUser = createNewUserRs.id;

		PostUsersRs getOneUser = UsersAdapter.getOneUser(idOfCreatedUser);
		assertEquals(getOneUser.id, idOfCreatedUser, "Incorrect user id returned.");
		assertEquals(getOneUser.firstname, requestBodyValues.getFirstname(), "Incorrect first name returned.");
	}
}