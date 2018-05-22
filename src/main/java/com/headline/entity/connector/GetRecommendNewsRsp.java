package com.headline.entity.connector;

import java.util.List;

import com.headline.entity.model.NewsModel;

public class GetRecommendNewsRsp extends RspBase{
	
	private List<NewsModel> modelList;

	public List<NewsModel> getModelList() {
		return modelList;
	}

	public void setModelList(List<NewsModel> modelList) {
		this.modelList = modelList;
	}
	
}
