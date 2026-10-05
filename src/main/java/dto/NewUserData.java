package dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class NewUserData {

	private final String email;
	private final String firstname;
	private final String lastname;
	private final String password;
	private final String avatar;
}