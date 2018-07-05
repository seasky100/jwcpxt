package com.pphgzs.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.pphgzs.domain.DO.jwcpxt_option;
import com.pphgzs.domain.DO.jwcpxt_question;
import com.pphgzs.domain.DO.jwcpxt_service_definition;
import com.pphgzs.domain.VO.QuestionVO;
import com.pphgzs.service.QuestionService;

@SuppressWarnings("serial")
public class QuestionAction extends ActionSupport implements ServletResponseAware, ServletRequestAware {

	private QuestionService questionService;
	private HttpServletResponse http_response;
	private HttpServletRequest http_request;

	/* 
	 * 
	 */
	private jwcpxt_question question;
	private jwcpxt_option option;
	private jwcpxt_service_definition serviceDefinition;
	private QuestionVO questionVO;
	private int moveOptionAction;
	private int moveQuestionAction;

	/*
	 * 创建选择题选项
	 * 
	 */
	public void add_option() {
		// TODO
	}

	/*
	 * 移动选择题选项
	 */
	public void move_option() {
		// TODO
	}

	/**
	 * 
	 */
	public void list_questionDTO_byServiceDefinitionID() {

	}

	/**
	 * 获取问题VO
	 * 
	 * @throws IOException
	 */
	public void get_questionVO() throws IOException {
		if (questionVO == null) {
			questionVO = new QuestionVO();
		}
		questionVO = questionService.get_questionVO(questionVO);
		//
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();// 格式化json数据
		Gson gson = gsonBuilder.serializeNulls().create();
		//
		http_response.setContentType("text/html;charset=utf-8");
		http_response.getWriter().write(gson.toJson(questionVO));
	}

	
	
	/*
	 *  
	 */
	@Override
	public void setServletRequest(HttpServletRequest http_request) {
		this.http_request = http_request;
	}

	@Override
	public void setServletResponse(HttpServletResponse http_response) {
		this.http_response = http_response;

	}

	public HttpServletResponse getHttp_response() {
		return http_response;
	}

	public void setHttp_response(HttpServletResponse http_response) {
		this.http_response = http_response;
	}

	public HttpServletRequest getHttp_request() {
		return http_request;
	}

	public void setHttp_request(HttpServletRequest http_request) {
		this.http_request = http_request;
	}

	public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}

	public jwcpxt_question getQuestion() {
		return question;
	}

	public void setQuestion(jwcpxt_question question) {
		this.question = question;
	}

	public jwcpxt_option getOption() {
		return option;
	}

	public void setOption(jwcpxt_option option) {
		this.option = option;
	}

	public int getMoveOptionAction() {
		return moveOptionAction;
	}

	public void setMoveOptionAction(int moveOptionAction) {
		this.moveOptionAction = moveOptionAction;
	}

	public int getMoveQuestionAction() {
		return moveQuestionAction;
	}

	public void setMoveQuestionAction(int moveQuestionAction) {
		this.moveQuestionAction = moveQuestionAction;
	}

	public jwcpxt_service_definition getServiceDefinition() {
		return serviceDefinition;
	}

	public void setServiceDefinition(jwcpxt_service_definition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
	}

	public QuestionVO getQuestionVO() {
		return questionVO;
	}

	public void setQuestionVO(QuestionVO questionVO) {
		this.questionVO = questionVO;
	}

}
