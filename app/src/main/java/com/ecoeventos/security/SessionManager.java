package com.ecoeventos.security;

import com.ecoeventos.bs.dao.impl.SugarDAOImpl;
import com.ecoeventos.bs.dao.SugarDAO;
import com.ecoeventos.eis.dto.UserDTO;
import com.orm.SugarApp;

import android.app.Application;

public class SessionManager extends Application{

	private UserDTO user;

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
}
