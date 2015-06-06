package com.ecoeventos.security;

import com.ecoeventos.bs.dao.impl.SugarDAOImpl;
import com.ecoeventos.bs.dao.SugarDAO;
import com.orm.SugarApp;

import android.app.Application;

public class SessionManager extends Application{

	private String username;
	private Integer id;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
