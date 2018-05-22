package com.headline.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.headline.core.SerializedField;
import com.headline.entity.News;
import com.headline.entity.connector.GetRecommendNewsReq;
import com.headline.entity.connector.GetRecommendNewsRsp;
import com.headline.entity.model.NewsModel;
import com.headline.service.NewsService;

/**
 * @Controller的方式实现如果要返回json，xml等文本，需要额外添加@ResponseBody注解
 * @RestController方式不需要写@ResponseBody，但是不能返回模型绑定数据和jsp视图，只能返回json，xml文本，仅仅是为了更加方便返回json资源而已。
 */
@RestController
public class NewsController {
	
	private static Logger logger=LoggerFactory.getLogger(NewsController.class);
	
	@Autowired
	private NewsService newsService;
	
	@RequestMapping("/findById/{id}")
	@SerializedField
	public News findById(@PathVariable String id) {
		return newsService.findById(id);
	}
	
	@RequestMapping("/findAll")
	@SerializedField
	public List<News> findAll(){
		return newsService.findAll();
	}
	
	@RequestMapping("/insert")
	@SerializedField(encode=false)
	public String insert(@RequestBody News news){
		return "success";
	}
	
	@RequestMapping(value="/getRecommendNews.do",method=RequestMethod.POST)
	@SerializedField
	public GetRecommendNewsRsp getRecommendNews(@RequestBody GetRecommendNewsReq getRecommendNewsReq){
		String type=getRecommendNewsReq.getType();
		logger.info("getRecommendNews,type:"+type);
		GetRecommendNewsRsp rspData=new GetRecommendNewsRsp();
		List<NewsModel> newsModelList=newsService.getRecommendNews(type);
		rspData.setModelList(newsModelList);
		rspData.setReturnCode("00000");
		rspData.setReturnMsg("交易成功");
		return rspData;
	}
}
