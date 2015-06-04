package com.bytesw.consultadecuentas.bs.dao.impl;

import java.util.List;

import com.bytesw.consultadecuentas.bs.dao.SugarDAO;
import com.bytesw.consultadecuentas.eis.bo.BaseBO;
import com.orm.SugarRecord;
import com.orm.query.Select;

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
