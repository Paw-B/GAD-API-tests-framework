package api_adapters;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;

public class UsersAdapter extends BaseAdapter {

	private static final String PATH = "/api/users";

	@Step("Get 200 from GET /users test")
	public static int get200FromGETUsersRequest() {
		return given()
				.baseUri(CONFIG.baseUrl())
				.log().all()
				.get(PATH)
				.then()
				.spec(ok200)
				.log().ifValidationFails()
				.extract()
				.statusCode();
	}
}