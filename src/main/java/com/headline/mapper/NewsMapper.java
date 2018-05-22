package com.headline.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.headline.entity.News;
import com.headline.entity.model.NewsModel;

@Mapper
public interface NewsMapper {
	
	@Select("select * from t_news where news_id = #{id}")
	News findById(String id);
	
	@Select("select * from t_news")
	List<News> findAll();
	
	@Insert("insert ignore into t_news values (#{news_id}, #{title}, #{news_time}, #{auth_name}, #{news_url}, #{pic_url_1}, #{pic_url_2}, #{pic_url_3}, #{category}, #{create_time}, #{source})")
	void insert(News news);
	
	@Select("select * from (select * from t_news where create_time>=#{0} and category=#{1} order by rand() limit 7) a order by a.news_time desc")
	List<NewsModel> getRecommendNews(String time,String type);
}
