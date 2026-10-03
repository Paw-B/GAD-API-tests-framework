package models.users;

import com.google.gson.annotations.Expose;

public class PostUsersRs {

	@Expose
	public int id;
	@Expose
	public String email;
	@Expose
	public String firstname;
	@Expose
	public String lastname;
	@Expose
	public String password;
	@Expose
	public String avatar;
}