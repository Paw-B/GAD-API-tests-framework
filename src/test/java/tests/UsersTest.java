package tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertTrue;

import api_adapters.UsersAdapter;
import io.restassured.response.Response;
import org.testng.annotations.Test;

@Test(testName = "Users API Tests")
public class UsersTest extends BaseTest {

	@Test(testName = "Verify GET /users against schema")
	public void verifyGetUsersResponseAgainstSchema() {
		Response getUsersResponse = UsersAdapter.getUsersRequest();

		assertTrue(
				matchesJsonSchemaInClasspath("json_schemas/get_users_schema.json")
						.matches(getUsersResponse.getBody().asString()),
				"Response for GET /users does not match the schema.");
	}
}