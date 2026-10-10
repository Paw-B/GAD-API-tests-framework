package models.login;

import com.google.gson.annotations.*;

public class LoginRs {

	@Expose
	@SerializedName("access_token")
	public String accessToken;
}