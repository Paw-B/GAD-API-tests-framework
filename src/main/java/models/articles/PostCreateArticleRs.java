package models.articles;

import com.google.gson.annotations.*;

public class PostCreateArticleRs {

	@Expose
	public int id;
	@Expose
	@SerializedName("user_id")
	public int userId;
	@Expose
	public String title;
	@Expose
	public String body;
	@Expose
	public String date;
	@Expose
	public String image;
}
