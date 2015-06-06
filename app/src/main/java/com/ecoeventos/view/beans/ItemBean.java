package com.ecoeventos.view.beans;

public class ItemBean {

	private String title;
	private String user;
	private String detail;
	private String rate;

	public ItemBean() {
		super();
	}

	public ItemBean(String title,String user, String detail, String rate) {
		super();
		this.title = title;
		this.user = user;
		this.detail = detail;
		this.rate = rate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
}
