package com.headline.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.headline.entity.News;
import com.headline.service.NewsService;

/**
 * 多线程执行定时任务
 */
@Component
public class ScheduledTasks implements SchedulingConfigurer{
	
	private static Logger logger=LoggerFactory.getLogger(ScheduledTasks.class);
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private NewsService newsService;
	
	private String url="http://toutiao-ali.juheapi.com/toutiao/index?type=";
	private String authorization="APPCODE ffdd9269fb76484294c62a1a7aa8fd04";
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		//设定一个长度10的定时任务线程池
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
	}
	
	/**
	 * 头条新闻
	 */
	@Scheduled(cron = "0 1/30 8-23 * * *")  
	public void getTopNews() {
		logger.info("--->查询头条新闻");
		getNewsData("top");
    }
	
	/**
	 * 社会新闻
	 */
	@Scheduled(cron = "0 2/30 8-23 * * *")  
	public void getShehuiNews() {
		logger.info("--->查询社会新闻");
		getNewsData("shehui");
	}
	
	/**
	 * 国内新闻
	 */
	@Scheduled(cron = "0 3/30 8-23 * * *")  
	public void getGuoneiNews() {
		logger.info("--->查询国内新闻");
		getNewsData("guonei");
	}
	
	/**
	 * 国际新闻
	 */
	@Scheduled(cron = "0 4 8-23 * * *")  
	public void getGuojiNews() {
		logger.info("--->查询国际新闻");
		getNewsData("guoji");
	}
	
	/**
	 * 娱乐新闻
	 */
	@Scheduled(cron = "0 5 8-23 * * *")  
	public void getYuleNews() {
		logger.info("--->查询娱乐新闻");
		getNewsData("yule");
	}
	
	/**
	 * 体育新闻
	 */
	@Scheduled(cron = "0 6 8-23 * * *")  
	public void getTiyuNews() {
		logger.info("--->查询体育新闻");
		getNewsData("tiyu");
	}
	
	/**
	 * 军事新闻
	 */
	@Scheduled(cron = "0 7 8-23 * * *")  
	public void getJunshiNews() {
		logger.info("--->查询军事新闻");
		getNewsData("junshi");
	}
	
	/**
	 * 科技新闻
	 */
	@Scheduled(cron = "0 8 10,14,18,22 * * *")  
	public void getKejiNews() {
		logger.info("--->查询科技新闻");
		getNewsData("keji");
	}
	
	/**
	 * 财经新闻
	 */
	@Scheduled(cron = "0 9 10,14,18,22 * * *")  
	public void getCaijingNews() {
		logger.info("--->查询财经新闻");
		getNewsData("caijing");
	}
	
	/**
	 * 时尚新闻
	 */
	@Scheduled(cron = "0 10 10,14,18,22 * * *")  
	public void getShishangNews() {
		logger.info("--->查询时尚新闻");
		getNewsData("shishang");
	}
	
	private void getNewsData(String type){
		logger.info("---->定时请求新闻头条type="+type+" start");
		String reqUrl=url+type;
		logger.info("请求url:"+reqUrl);
		HttpHeaders headers=new HttpHeaders();
		headers.set("Authorization", authorization);
		HttpEntity<String> entity=new HttpEntity<String>(headers);
		ResponseEntity<String> response=restTemplate.exchange(reqUrl, HttpMethod.GET, entity, String.class);
		String returnData = response.getBody();
		logger.info("返回数据:"+returnData);
		List<News> newsList=parseReturnData(returnData);
		newsService.insertNews(newsList);
		logger.info("---->定时请求新闻头条type="+type+" end");
	}
	
	private List<News> parseReturnData(String returnData){
		List<News> newsList=new ArrayList<News>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		JSONObject jsonObj=JSONObject.parseObject(returnData);
		JSONObject result=jsonObj.getJSONObject("result");
		JSONArray newsArr=result.getJSONArray("data");
		String create_time=sdf.format(new Date());
		for (int i = 0,len=newsArr.size(); i < len; i++) {
			JSONObject obj=newsArr.getJSONObject(i);
			News news=new News();
			news.setNews_id(obj.getString("uniquekey"));
			news.setTitle(obj.getString("title"));
			news.setNews_time(obj.getString("date"));
			news.setAuth_name(obj.getString("author_name"));
			news.setNews_url(obj.getString("url"));
			if(obj.containsKey("thumbnail_pic_s")){
				news.setPic_url_1(obj.getString("thumbnail_pic_s"));
			}
			if(obj.containsKey("thumbnail_pic_s02")){
				news.setPic_url_2(obj.getString("thumbnail_pic_s02"));
			}
			if(obj.containsKey("thumbnail_pic_s03")){
				news.setPic_url_3(obj.getString("thumbnail_pic_s03"));
			}
			news.setCategory(obj.getString("category"));
			news.setCreate_time(create_time);
			news.setSource("东方头条");
			newsList.add(news);
		}
		return newsList;
	}

	
}
