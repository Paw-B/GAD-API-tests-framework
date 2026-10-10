package api_adapters;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import models.users.*;

public class UsersAdapter extends BaseAdapter {

	private static final String PATH = "/api/users";

	@Step("Send GET /users request")
	public static Response getUsers() {
		return given()
				.spec(spec)
				.log().all()
				.get(PATH)
				.then()
				.spec(ok200)
				.log().ifValidationFails()
				.extract()
				.response();
	}

	@Step("Send POST /users request")
	public static PostCreateUserRs createUser(PostCreateUserRq request) {
		return given()
				.spec(spec)
				.log().all()
				.body(gson.toJson(request))
				.log().all()
				.when()
				.post(PATH)
				.then()
				.spec(ok201)
				.log().ifValidationFails()
				.extract()
				.as(PostCreateUserRs.class, ObjectMapperType.GSON);
	}

	@Step("Send GET /users/{id} request")
	public static PostCreateUserRs getUser(int userId) {
		return given()
				.spec(spec)
				.log().all()
				.get(PATH + "/" + userId)
				.then()
				.spec(ok200)
				.log().ifValidationFails()
				.assertThat()
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json_schemas/get_user_schema.json"))
				.extract()
				.as(PostCreateUserRs.class, ObjectMapperType.GSON);
	}

	@Step("Send PATCH /users/{id} request")
	public static void patchUser(PostCreateUserRq request, int userId, String accessToken) {
		given()
				.spec(spec)
				.header("Authorization", "Bearer " + accessToken)
				.log().all()
				.body(gson.toJson(request))
				.log().ifValidationFails()
				.patch(PATH + "/" + userId)
				.then()
				.spec(ok200)
				.log().ifValidationFails()
				.assertThat()
				.header("content-type", "application/json; charset=utf-8")
				.log().ifValidationFails();
	}

	@Step("Send DELETE /users/{id} request")
	public static int deleteUser(int userId, String accessToken) {
		return given()
				.spec(spec)
				.header("Authorization", "Bearer " + accessToken)
				.log().all()
				.delete(PATH + "/" + userId)
				.then()
				.spec(ok200)
				.log().ifValidationFails()
				.assertThat()
				.header("content-type", "application/json; charset=utf-8")
				.extract()
				.statusCode();
	}

	@Step("Send GET /users/{id} request to verify user does not exist")
	public static int getDeletedUser(int userId) {
		return given()
				.spec(spec)
				.log().all()
				.get(PATH + "/" + userId)
				.then()
				.spec(ok404)
				.log().ifValidationFails()
				.assertThat()
				.header("content-type", "application/json; charset=utf-8")
				.extract()
				.statusCode();
	}
}