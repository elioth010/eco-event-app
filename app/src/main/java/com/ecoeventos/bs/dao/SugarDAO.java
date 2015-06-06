package com.ecoeventos.bs.dao;

import com.ecoeventos.eis.bo.BaseBO;

import java.util.List;

public interface SugarDAO {
	
	public <T> BaseBO save(BaseBO baseBO);

	public void update(BaseBO baseBO);

	public void delete(BaseBO baseBO);

	public <T> BaseBO findById(Class<? extends BaseBO> clazz, Integer id);

	public <T> List<T> findAll(Class<? extends BaseBO> clazz);

	public <T> List<T> findBySQLQuery(Class<? extends BaseBO> clazz, String query);
}
