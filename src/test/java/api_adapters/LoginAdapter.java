package api_adapters;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import models.login.*;

public class LoginAdapter extends BaseAdapter {

	private static final String PATH = "/api/login";

	@Step("Send POST /login request")
	public static LoginRs postLogin(LoginRq rq) {
		return given()
				.spec(spec)
				.log().all()
				.body(gson.toJson(rq))
				.log().all()
				.post(PATH)
				.then()
				.spec(ok200or201)
				.log().all()
				.extract()
				.as(LoginRs.class);
	}
}