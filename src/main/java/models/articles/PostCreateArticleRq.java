package models.articles;

import com.google.gson.annotations.*;
import lombok.*;

/**
 * Request body shared by POST, PUT and PATCH /articles. Fields left null are
 * not serialized, so the same model
 * covers full payloads (POST/PUT) and partial payloads (PATCH). {@code user_id}
 * is deliberately absent: the API
 * takes the owner from the access token.
 */
@Data
@Builder
public class PostCreateArticleRq {

	@Expose
	private String title;
	@Expose
	private String body;
	@Expose
	private String date;
	@Expose
	private String image;
}
