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
				".\\data\\images\\256\\test_image_" + (int) (Math.random() * 1000000000 + 1) + ".jpg");
	}

	// GAD rejects dates "in the future", and its cut-off moves with the UTC
	// offset of the machine the server runs on. "Now" in UTC gets a 422 when
	// that machine is west of UTC; one day in the past is accepted everywhere.
	private static String articleDate() {
		return Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS).toString();
	}
}
