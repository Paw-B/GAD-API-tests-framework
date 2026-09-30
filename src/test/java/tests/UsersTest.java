package tests;

import static org.testng.Assert.assertEquals;

import api_adapters.UsersAdapter;
import org.testng.annotations.Test;

@Test(testName = "Users API Tests")
public class UsersTest extends BaseTest {

	@Test(testName = "Get 200 status from GET /users")
	public void get200FromGETUsers() {
		int statusCode = UsersAdapter.get200FromGETUsersRequest();

		assertEquals(statusCode, 200, "Not 200");
	}
}