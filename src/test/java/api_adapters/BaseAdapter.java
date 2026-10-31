package api_adapters;

import com.google.gson.*;
import config.TestConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.*;
import io.restassured.http.ContentType;
import io.restassured.specification.*;
import org.aeonbits.owner.ConfigFactory;

public class BaseAdapter {

	public static final TestConfig CONFIG = ConfigFactory.create(TestConfig.class);
	private static final AllureRestAssured ALLURE_FILTER = new AllureRestAssured();

	public static RequestSpecification spec = new RequestSpecBuilder()
			.setBaseUri(CONFIG.baseUrl())
			.setContentType(ContentType.JSON)
			.addFilter(ALLURE_FILTER)
			.build();

	static Gson gson = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation()
			.create();

	public static ResponseSpecification ok200 = new ResponseSpecBuilder()
			.expectStatusCode(200)
			.build();

	public static ResponseSpecification ok201 = new ResponseSpecBuilder()
			.expectStatusCode(201)
			.build();

	public static ResponseSpecification ok404 = new ResponseSpecBuilder()
			.expectStatusCode(404)
			.build();
}