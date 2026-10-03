package dto;

import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;

@Log4j2
public class NewUserDataFactory {

	private static final Faker faker = new Faker();

	public static NewUserData createNewUserData() {
		log.info("Creating Project data");
		return new NewUserData(
				faker.internet().emailAddress(),
				faker.name().firstName(),
				faker.name().lastName(),
				faker.lorem().characters(7),
				".\\data\\users\\face_" + (int) (Math.random() * 1000000000 + 1)
						+ "." + (int) (Math.random() * 100000 + 1) + ".jpg");
	}
}