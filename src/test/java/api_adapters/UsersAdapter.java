package api_adapters;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UsersAdapter extends BaseAdapter {

	private static final String PATH = "/api/users";

	@Step("Send GET /users request")
	public static Response getUsersRequest() {
		return given()
				.baseUri(CONFIG.baseUrl())
				.log().ifValidationFails()
				.get(PATH)
				.then()
				.spec(ok200)
				.log().ifValidationFails()
				.extract()
				.response();
	}
}