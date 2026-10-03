package tests;

import api_adapters.UsersAdapter;
import dto.NewUserData;
import io.qameta.allure.testng.AllureTestNg;
import listeners.TestListener;
import models.users.*;
import org.testng.annotations.Listeners;

@Listeners({ AllureTestNg.class, TestListener.class })
public class BaseTest {

	public PostUsersRs postCreateNewUser(NewUserData requestBodyValues) {

		PostCreateNewUserRq createNewUserRq = PostCreateNewUserRq
				.builder()
				.email(requestBodyValues.getEmail())
				.firstName(requestBodyValues.getFirstname())
				.lastName(requestBodyValues.getLastname())
				.password(requestBodyValues.getPassword())
				.avatar(requestBodyValues.getAvatar())
				.build();

		return UsersAdapter.createNewUser(createNewUserRq);
	}
}