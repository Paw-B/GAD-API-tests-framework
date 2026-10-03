package api_adapters;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import com.google.gson.*;
import config.TestConfig;
import io.restassured.builder.*;
import io.restassured.http.ContentType;
import io.restassured.specification.*;
import org.aeonbits.owner.ConfigFactory;

public class BaseAdapter {

	public static final TestConfig CONFIG = ConfigFactory.create(TestConfig.class);

	public static RequestSpecification spec = new RequestSpecBuilder()
			.setBaseUri(CONFIG.baseUrl())
			.setContentType(ContentType.JSON)
			.build();

	static Gson gson = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation()
			.create();

	public static ResponseSpecification ok200or201 = new ResponseSpecBuilder()
			.expectStatusCode(anyOf(is(200), is(201)))
			.build();

	public static ResponseSpecification ok404 = new ResponseSpecBuilder()
			.expectStatusCode(404)
			.build();
}