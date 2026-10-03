package api_adapters;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.users.*;

public class UsersAdapter extends BaseAdapter {

	private static final String PATH = "/api/users";

	@Step("Send GET /users request")
	public static Response getUsersRequest() {
		return given()
				.spec(spec)
				.log().all()
				.get(PATH)
				.then()
				.spec(ok200or201)
				.log().all()
				.extract()
				.response();
	}

	@Step("Send POST /users request")
	public static PostUsersRs createNewUser(PostCreateNewUserRq rq) {
		return given()
				.spec(spec)
				.body(gson.toJson(rq))
				.log().all()
				.when()
				.post(PATH)
				.then()
				.spec(ok200or201)
				.log().all()
				.extract()
				.as(PostUsersRs.class);
	}
}