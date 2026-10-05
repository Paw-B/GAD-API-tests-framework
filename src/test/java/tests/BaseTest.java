package tests;

import static api_adapters.UsersAdapter.patchUser;

import api_adapters.LoginAdapter;
import api_adapters.UsersAdapter;
import dto.NewUserData;
import io.qameta.allure.testng.AllureTestNg;
import listeners.TestListener;
import models.login.LoginRq;
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

	public void patchNewUser(NewUserData requestBodyValuesForNewUser,
			NewUserData requestBodyValuesForPatchRequestValues, int idOfCreatedUser) {
		PostCreateNewUserRq createNewDataForUser = PostCreateNewUserRq
				.builder()
				.email(requestBodyValuesForNewUser.getEmail())
				.firstName(requestBodyValuesForNewUser.getFirstname())
				.lastName(requestBodyValuesForNewUser.getLastname())
				.password(requestBodyValuesForNewUser.getPassword())
				.avatar(requestBodyValuesForNewUser.getAvatar())
				.build();

		LoginRq loginRq = LoginRq
				.builder()
				.email(requestBodyValuesForPatchRequestValues.getEmail())
				.password(requestBodyValuesForPatchRequestValues.getPassword())
				.build();

		String authToken = LoginAdapter.postLogin(loginRq).access_token;

		patchUser(createNewDataForUser, idOfCreatedUser, authToken);
	}

	public String createAccessToken(NewUserData requestBodyValues) {
		LoginRq loginRq = LoginRq
				.builder()
				.email(requestBodyValues.getEmail())
				.password(requestBodyValues.getPassword())
				.build();

		return LoginAdapter.postLogin(loginRq).access_token;
	}
}