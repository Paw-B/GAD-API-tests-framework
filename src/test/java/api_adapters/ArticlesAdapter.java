package api_adapters;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import models.articles.*;

public class ArticlesAdapter extends BaseAdapter {

	private static final String PATH = "/api/articles";

	@Step("Send GET /articles request")
	public static Response getArticles() {
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

	@Step("Send POST /articles request")
	public static PostCreateArticleRs createArticle(PostCreateArticleRq request, String accessToken) {
		return given()
				.spec(spec)
				.header("Authorization", "Bearer " + accessToken)
				.log().all()
				.body(gson.toJson(request))
				.log().all()
				.when()
				.post(PATH)
				.then()
				.spec(ok201)
				.log().ifValidationFails()
				.extract()
				.as(PostCreateArticleRs.class, ObjectMapperType.GSON);
	}

	@Step("Send GET /articles/{id} request")
	public static PostCreateArticleRs getArticle(int articleId) {
		return given()
				.spec(spec)
				.log().all()
				.get(PATH + "/" + articleId)
				.then()
				.spec(ok200)
				.log().ifValidationFails()
				.assertThat()
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json_schemas/get_article_schema.json"))
				.extract()
				.as(PostCreateArticleRs.class, ObjectMapperType.GSON);
	}

	@Step("Send PATCH /articles/{id} request")
	public static void patchArticle(PostCreateArticleRq request, int articleId, String accessToken) {
		given()
				.spec(spec)
				.header("Authorization", "Bearer " + accessToken)
				.log().all()
				.body(gson.toJson(request))
				.log().ifValidationFails()
				.patch(PATH + "/" + articleId)
				.then()
				.spec(ok200)
				.log().ifValidationFails()
				.assertThat()
				.header("content-type", "application/json; charset=utf-8")
				.log().ifValidationFails();
	}

	@Step("Send DELETE /articles/{id} request")
	public static int deleteArticle(int articleId, String accessToken) {
		return given()
				.spec(spec)
				.header("Authorization", "Bearer " + accessToken)
				.log().all()
				.delete(PATH + "/" + articleId)
				.then()
				.spec(ok200)
				.log().ifValidationFails()
				.assertThat()
				.header("content-type", "application/json; charset=utf-8")
				.extract()
				.statusCode();
	}

	@Step("Send GET /articles/{id} request to verify article does not exist")
	public static int getDeletedArticle(int articleId) {
		return given()
				.spec(spec)
				.log().all()
				.get(PATH + "/" + articleId)
				.then()
				.spec(ok404)
				.log().ifValidationFails()
				.assertThat()
				.header("content-type", "application/json; charset=utf-8")
				.extract()
				.statusCode();
	}
}
