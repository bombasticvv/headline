package com.headline.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.headline.entity.News;
import com.headline.entity.model.NewsModel;
import com.headline.mapper.NewsMapper;

@Service
public class NewsService {
	
	@Autowired
	private NewsMapper newsMapper;
	
	public News findById(String id) {
		return newsMapper.findById(id);
	}
	
	public List<News> findAll(){
		return newsMapper.findAll();
	}
	
	@Transactional //添加事务
	public void insertNews(List<News> newsList){
		for (News news : newsList) {
			newsMapper.insert(news);
		}
	}
	
	public List<NewsModel> getRecommendNews(String type){
		Date sdate=DateUtils.addDays(new Date(), -7);
		String time=DateFormatUtils.format(sdate, "yyyy-MM-dd");
		switch (type) {
		case "top":
			type="头条";
			break;
		case "shehui":
			type="社会";
			break;
		case "guonei":
			type="国内";
			break;
		case "guoji":
			type="国际";
			break;
		case "yule":
			type="娱乐";
			break;
		case "tiyu":
			type="体育";
			break;
		case "junshi":
			type="军事";
			break;
		case "keji":
			type="科技";
			break;
		case "caijing":
			type="财经";
			break;
		case "shishang":
			type="时尚";
			break;
		default:
			type="头条";
			break;
		}
		List<NewsModel> newsModelList=newsMapper.getRecommendNews(time,type);
		return newsModelList;
	}
}
