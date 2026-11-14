package models.articles;

import com.google.gson.annotations.*;
import lombok.*;

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