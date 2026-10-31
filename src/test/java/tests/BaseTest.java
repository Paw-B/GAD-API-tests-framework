package tests;

import api_adapters.*;
import dto.NewUserData;
import io.qameta.allure.testng.AllureTestNg;
import listeners.TestListener;
import models.login.LoginRq;
import models.users.*;
import org.testng.annotations.*;

@Listeners({ AllureTestNg.class, TestListener.class })
public class BaseTest {

	protected Integer userId;
	protected NewUserData requestBody;

	public PostCreateUserRs postCreateUser(NewUserData requestBody) {

		PostCreateUserRq createNewUserRequest = PostCreateUserRq
				.builder()
				.email(requestBody.getEmail())
				.firstName(requestBody.getFirstName())
				.lastName(requestBody.getLastName())
				.password(requestBody.getPassword())
				.avatar(requestBody.getAvatar())
				.build();

		PostCreateUserRs createdUser = UsersAdapter.createUser(createNewUserRequest);
		this.userId = createdUser.id;
		this.requestBody = requestBody;
		return createdUser;
	}

	public void patchUser(NewUserData requestBodyNewUser,
			NewUserData requestBodyForPatchRequest, int userId) {
		PostCreateUserRq createNewDataForUser = PostCreateUserRq
				.builder()
				.email(requestBodyNewUser.getEmail())
				.firstName(requestBodyNewUser.getFirstName())
				.lastName(requestBodyNewUser.getLastName())
				.password(requestBodyNewUser.getPassword())
				.avatar(requestBodyNewUser.getAvatar())
				.build();

		LoginRq loginRequest = LoginRq
				.builder()
				.email(requestBodyForPatchRequest.getEmail())
				.password(requestBodyForPatchRequest.getPassword())
				.build();

		String authToken = LoginAdapter.postLogin(loginRequest).accessToken;

		UsersAdapter.patchUser(createNewDataForUser, userId, authToken);

		if (this.userId != null && this.userId == userId) {
			this.requestBody = requestBodyNewUser;
		}
	}

	protected void markCreatedUserDeleted(int userId) {
		if (this.userId != null && this.userId == userId) {
			this.userId = null;
			requestBody = null;
		}
	}

	public String getAccessToken(NewUserData requestBody) {
		LoginRq loginRequest = LoginRq
				.builder()
				.email(requestBody.getEmail())
				.password(requestBody.getPassword())
				.build();

		return LoginAdapter.postLogin(loginRequest).accessToken;
	}
}