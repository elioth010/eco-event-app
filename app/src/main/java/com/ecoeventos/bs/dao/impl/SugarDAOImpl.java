package com.ecoeventos.bs.dao.impl;

import com.ecoeventos.bs.dao.SugarDAO;
import com.ecoeventos.eis.bo.BaseBO;
import com.orm.query.Select;

import java.util.List;

public class SugarDAOImpl implements SugarDAO {

    public SugarDAOImpl() {
        super();
    }

    @Override
    public <T> BaseBO save(BaseBO baseBO) {
        baseBO.save();
        return baseBO;
    }

    @Override
    public void update(BaseBO baseBO) {
        baseBO.save();
    }

    @Override
    public void delete(BaseBO baseBO) {
        baseBO.delete();
    }

    @Override
    public <T> BaseBO findById(Class<? extends BaseBO> clazz, Integer id) {
        return BaseBO.findById(clazz, id.longValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAll(Class<? extends BaseBO> clazz) {
        return (List<T>) Select.from(clazz).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findBySQLQuery(Class<? extends BaseBO> clazz, String query) {
        return (List<T>) BaseBO.findWithQuery(clazz, query);
    }
}
