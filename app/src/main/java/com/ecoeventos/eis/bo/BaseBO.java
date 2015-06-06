package com.ecoeventos.eis.bo;

import com.orm.SugarRecord;

import java.io.Serializable;

public class BaseBO<T> extends SugarRecord<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8192030381945067985L;
	
	public BaseBO() {
		super();
	}
	
}
