package dto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;

@Log4j2
public class NewArticleDataFactory {

	private static final Faker faker = new Faker();

	public static NewArticleData createNewArticleData() {
		log.info("Creating new article data");
		return new NewArticleData(
				faker.lorem().sentence(4),
				faker.lorem().paragraph(3),
				articleDate(),
				".\\data\\images\\article_attachment_test_image_" + (int) (Math.random() * 1000000000 + 1) + ".jpg");
	}

	private static String articleDate() {
		return Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS).toString();
	}
}