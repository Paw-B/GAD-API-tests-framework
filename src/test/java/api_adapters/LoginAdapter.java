package api_adapters;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.mapper.ObjectMapperType;
import models.login.*;

public class LoginAdapter extends BaseAdapter {

	private static final String PATH = "/api/login";

	@Step("Send POST /login request")
	public static LoginRs postLogin(LoginRq request) {
		return given()
				.spec(spec)
				.log().all()
				.body(gson.toJson(request))
				.log().all()
				.post(PATH)
				.then()
				.spec(ok200)
				.log().ifValidationFails()
				.extract()
				.as(LoginRs.class, ObjectMapperType.GSON);
	}
}