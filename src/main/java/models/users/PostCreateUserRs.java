package models.users;

import com.google.gson.annotations.*;

public class PostCreateUserRs {

	@Expose
	public int id;
	@Expose
	public String email;
	@Expose
	@SerializedName("firstname")
	public String firstName;
	@Expose
	@SerializedName("lastname")
	public String lastName;
	@Expose
	public String password;
	@Expose
	public String avatar;
}