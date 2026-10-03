package models.login;

import com.google.gson.annotations.Expose;
import lombok.*;

@Data
@Builder
public class LoginRq {

	@Expose
	private String email;
	@Expose
	private String password;
}