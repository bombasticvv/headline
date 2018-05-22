package com.headline.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.headline.entity.connector.Data;
import com.headline.utils.AESOperator;

@ControllerAdvice(basePackages = "com.headline.controller")
public class MyResponseBodyAdvice implements ResponseBodyAdvice{
	
	private static Logger logger=LoggerFactory.getLogger(MyResponseBodyAdvice.class);
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		boolean encode = false;
		if(returnType.getMethod().isAnnotationPresent(SerializedField.class)){
			SerializedField serializedField=returnType.getMethodAnnotation(SerializedField.class);
			encode = serializedField.encode();
		}
		if(encode){
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				String content=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
				logger.info("加密原数据："+content);
				AESOperator aes=AESOperator.getInstance();
				String randomKey=aes.generateString();
				logger.info("随机密钥:"+randomKey);
				String result=aes.encrypt(content, randomKey);
				logger.info("加密后数据："+result);
				Data data=new Data();
				data.setD(result);
				data.setT(randomKey);
				return data;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return body;
	}
	
	/**
	 * controller中存在@ResponseBody时
	 * 先执行supports方法，return true再执行beforeBodyWrite方法，return false则不执行
	 */
	@Override
	public boolean supports(MethodParameter arg0, Class arg1) {
		return true;
	}

}
