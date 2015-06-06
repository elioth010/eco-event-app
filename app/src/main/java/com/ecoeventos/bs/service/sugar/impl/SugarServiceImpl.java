package com.ecoeventos.bs.service.sugar.impl;

import com.ecoeventos.bs.dao.SugarDAO;
import com.ecoeventos.bs.service.sugar.SugarService;
import com.ecoeventos.eis.bo.BaseBO;

import java.util.List;

public class SugarServiceImpl implements SugarService {

    private SugarDAO sugarDAO;

    public SugarServiceImpl(SugarDAO daoImpl) {
        sugarDAO = daoImpl;
    }

    @Override
    public BaseBO save(BaseBO baseBO) {
        return getSugarDAO().save(baseBO);
    }

    @Override
    public void update(BaseBO baseBO) {
        getSugarDAO().update(baseBO);
    }

    @Override
    public void delete(BaseBO baseBO) {
        getSugarDAO().delete(baseBO);
    }

    @Override
    public <T> BaseBO findById(Class<? extends BaseBO> clazz, Integer id) {
        return getSugarDAO().findById(clazz, id);
    }

    @Override
    public <T> List<T> findAll(Class<? extends BaseBO> clazz) {
        return getSugarDAO().findAll(clazz);
    }

    @Override
    public <T> List<T> findBySQLQuery(Class<? extends BaseBO> clazz, String query) {
        return getSugarDAO().findBySQLQuery(clazz, query);
    }

    public SugarDAO getSugarDAO() {
        return sugarDAO;
    }

    public void setSugarDAO(SugarDAO sugarDAO) {
        this.sugarDAO = sugarDAO;
    }

}
