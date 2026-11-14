package dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class NewArticleData {

	private final String title;
	private final String body;
	private final String date;
	private final String image;
}