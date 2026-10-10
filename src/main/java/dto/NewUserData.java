package dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class NewUserData {

	private final String email;
	@SerializedName("firstname")
	private final String firstName;
	@SerializedName("lastname")
	private final String lastName;
	private final String password;
	private final String avatar;
}