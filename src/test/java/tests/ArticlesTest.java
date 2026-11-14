package tests;

import static api_adapters.ArticlesAdapter.*;
import static dto.NewArticleDataFactory.createNewArticleData;
import static dto.NewUserDataFactory.createNewUserData;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.*;

import api_adapters.UsersAdapter;
import dto.NewArticleData;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.articles.*;
import org.testng.annotations.*;

@Test(testName = "Articles API Tests")
public class ArticlesTest extends BaseTest {

	private String ownerToken;

	@BeforeMethod(alwaysRun = true)
	public void createArticleOwner() {
		postCreateUser(createNewUserData());
		ownerToken = getAccessToken(requestBody);
	}

	@AfterMethod(alwaysRun = true)
	public void deleteCreatedTestData() {
		try {
			deleteCreatedArticles();
		} finally {
			if (userId != null && requestBody != null) {
				UsersAdapter.deleteUser(userId, getAccessToken(requestBody));
				markCreatedUserDeleted(userId);
			}
		}
	}

	@Feature("Articles")
	@Severity(SeverityLevel.CRITICAL)
	@Test(testName = "Verify GET /articles against schema")
	public void verifyGetArticlesResponseAgainstSchema() {
		Response getArticlesResponse = getArticles();

		assertTrue(
				matchesJsonSchemaInClasspath("json_schemas/get_articles_schema.json")
						.matches(getArticlesResponse.getBody().asString()),
				"Response for GET /articles does not match the schema.");
	}

	@Feature("Articles")
	@Severity(SeverityLevel.CRITICAL)
	@Test(testName = "Verify POST /articles creates new article", priority = 1)
	public void verifyPostArticleRequest() {
		NewArticleData articleData = createNewArticleData();
		PostCreateArticleRs createArticleResponse = postCreateArticle(articleData, ownerToken);

		assertTrue(createArticleResponse.id > 0, "Id was not assigned.");
		assertEquals(createArticleResponse.userId, userId.intValue(),
				"Article was not assigned to the logged in user.");
		assertEquals(createArticleResponse.title, articleData.getTitle(), "Incorrect title returned.");
		assertEquals(createArticleResponse.body, articleData.getBody(), "Incorrect body returned.");
		assertEquals(createArticleResponse.date, articleData.getDate(), "Incorrect date returned.");
		assertEquals(createArticleResponse.image, articleData.getImage(), "Incorrect image returned.");
	}

	@Feature("Articles")
	@Severity(SeverityLevel.NORMAL)
	@Test(testName = "Verify GET /articles/{id} returns one article", priority = 2)
	public void verifyGetArticleRequest() {
		NewArticleData articleData = createNewArticleData();
		PostCreateArticleRs createArticleResponse = postCreateArticle(articleData, ownerToken);
		int articleId = createArticleResponse.id;

		PostCreateArticleRs getArticle = getArticle(articleId);
		assertEquals(getArticle.id, articleId, "Incorrect article id returned.");
		assertEquals(getArticle.title, articleData.getTitle(), "Incorrect title returned.");
	}

	@Feature("Articles")
	@Severity(SeverityLevel.CRITICAL)
	@Test(testName = "Verify PATCH /articles/{id} changes article's data", priority = 3)
	public void verifyPatchArticleRequest() {
		PostCreateArticleRs createArticleResponse = postCreateArticle(createNewArticleData(), ownerToken);
		int articleId = createArticleResponse.id;
		NewArticleData articleDataForPatch = createNewArticleData();

		patchArticle(toCreateArticleRq(articleDataForPatch), articleId, ownerToken);
		PostCreateArticleRs getUpdatedArticleResponse = getArticle(articleId);

		assertEquals(getUpdatedArticleResponse.title, articleDataForPatch.getTitle(), "Title was not patched");
		assertEquals(getUpdatedArticleResponse.body, articleDataForPatch.getBody(), "Body was not patched");
		assertEquals(getUpdatedArticleResponse.image, articleDataForPatch.getImage(), "Image was not patched");
	}

	@Feature("Articles")
	@Severity(SeverityLevel.CRITICAL)
	@Test(testName = "Verify DELETE /articles/{id} deletes article", priority = 4)
	public void verifyDeleteArticleRequest() {
		PostCreateArticleRs createArticleResponse = postCreateArticle(createNewArticleData(), ownerToken);
		int articleId = createArticleResponse.id;

		int deleteArticleResponseCode = deleteArticle(articleId, ownerToken);
		markCreatedArticleDeleted(articleId);

		assertEquals(deleteArticleResponseCode, 200, "Delete article failed");
		assertEquals(getDeletedArticle(articleId), 404, "Delete article failed");
	}
}