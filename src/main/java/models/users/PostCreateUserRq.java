package models.users;

import com.google.gson.annotations.*;
import lombok.*;

@Data
@Builder
public class PostCreateUserRq {

	@Expose
	private String email;
	@Expose
	@SerializedName("firstname")
	private String firstName;
	@Expose
	@SerializedName("lastname")
	private String lastName;
	@Expose
	private String password;
	@Expose
	private String avatar;
}