package com.pphgzs.domain.DO;

public class jwcpxt_answer_choice {
	private String jwcpxt_answer_choice_id;
	private String answer_choice_client;
	private String answer_choice_option;
	private String answer_choice_question;
	private String answer_choice_gmt_create;
	private String answer_choice_gmt_modified;

	public String getJwcpxt_answer_choice_id() {
		return jwcpxt_answer_choice_id;
	}

	public void setJwcpxt_answer_choice_id(String jwcpxt_answer_choice_id) {
		this.jwcpxt_answer_choice_id = jwcpxt_answer_choice_id;
	}

	public String getAnswer_choice_client() {
		return answer_choice_client;
	}

	public void setAnswer_choice_client(String answer_choice_client) {
		this.answer_choice_client = answer_choice_client;
	}

	public String getAnswer_choice_option() {
		return answer_choice_option;
	}

	public void setAnswer_choice_option(String answer_choice_option) {
		this.answer_choice_option = answer_choice_option;
	}

	public String getAnswer_choice_question() {
		return answer_choice_question;
	}

	public void setAnswer_choice_question(String answer_choice_question) {
		this.answer_choice_question = answer_choice_question;
	}

	public String getAnswer_choice_gmt_create() {
		return answer_choice_gmt_create;
	}

	public void setAnswer_choice_gmt_create(String answer_choice_gmt_create) {
		this.answer_choice_gmt_create = answer_choice_gmt_create;
	}

	public String getAnswer_choice_gmt_modified() {
		return answer_choice_gmt_modified;
	}

	public void setAnswer_choice_gmt_modified(String answer_choice_gmt_modified) {
		this.answer_choice_gmt_modified = answer_choice_gmt_modified;
	}

	@Override
	public String toString() {
		return "jwcpxt_answer_choice [jwcpxt_answer_choice_id=" + jwcpxt_answer_choice_id + ", answer_choice_client="
				+ answer_choice_client + ", answer_choice_option=" + answer_choice_option + ", answer_choice_question="
				+ answer_choice_question + ", answer_choice_gmt_create=" + answer_choice_gmt_create
				+ ", answer_choice_gmt_modified=" + answer_choice_gmt_modified + "]";
	}

}
