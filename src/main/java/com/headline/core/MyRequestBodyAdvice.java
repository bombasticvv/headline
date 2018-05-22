package com.headline.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.headline.entity.connector.Data;
import com.headline.utils.AESOperator;

@ControllerAdvice(basePackages = "com.headline.controller")
public class MyRequestBodyAdvice implements RequestBodyAdvice{
	
	private static Logger logger=LoggerFactory.getLogger(MyRequestBodyAdvice.class);
	
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}
	
	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage,
			MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}
	
	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
			MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		boolean decode = false;
		if(methodParameter.getMethod().isAnnotationPresent(SerializedField.class)){
			SerializedField serializedField=methodParameter.getMethodAnnotation(SerializedField.class);
			decode=serializedField.decode();
		}
		if(decode){
			try {
	            return new MyHttpInputMessage(inputMessage);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		return inputMessage;
	}
	
	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
			MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		logger.info("--->RequestBodyAdvice  afterBodyRead");
		return body;
	}

	class MyHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            ObjectMapper objectMapper = new ObjectMapper();
            Data data=objectMapper.readValue(inputMessage.getBody(), Data.class);
            String randomKey=data.getT();
            String content=data.getD();
            logger.info("key:"+randomKey);
            logger.info("content:"+content);
            this.body = IOUtils.toInputStream(AESOperator.getInstance().decrypt(content, randomKey));
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }

}
