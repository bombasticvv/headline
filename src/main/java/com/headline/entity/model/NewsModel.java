package com.headline.entity.model;

public class NewsModel {
	private String news_id;
	private String title;
	private String news_time;
	private String auth_name;
	private String pic_url_1;
	private String pic_url_2;
	private String pic_url_3;
	private String news_url;
	private String category;

	public String getNews_id() {
		return news_id;
	}

	public void setNews_id(String news_id) {
		this.news_id = news_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNews_time() {
		return news_time;
	}

	public void setNews_time(String news_time) {
		this.news_time = news_time;
	}

	public String getAuth_name() {
		return auth_name;
	}

	public void setAuth_name(String auth_name) {
		this.auth_name = auth_name;
	}

	public String getPic_url_1() {
		return pic_url_1;
	}

	public void setPic_url_1(String pic_url_1) {
		this.pic_url_1 = pic_url_1;
	}

	public String getPic_url_2() {
		return pic_url_2;
	}

	public void setPic_url_2(String pic_url_2) {
		this.pic_url_2 = pic_url_2;
	}

	public String getPic_url_3() {
		return pic_url_3;
	}

	public void setPic_url_3(String pic_url_3) {
		this.pic_url_3 = pic_url_3;
	}

	public String getNews_url() {
		return news_url;
	}

	public void setNews_url(String news_url) {
		this.news_url = news_url;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "NewsModel [news_id=" + news_id + ", title=" + title
				+ ", news_time=" + news_time + ", auth_name=" + auth_name
				+ ", pic_url_1=" + pic_url_1 + ", pic_url_2=" + pic_url_2
				+ ", pic_url_3=" + pic_url_3 + ", news_url=" + news_url
				+ ", category=" + category + "]";
	}

}
