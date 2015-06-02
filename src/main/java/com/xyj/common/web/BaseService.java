package com.xyj.common.web;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @classDescription:增删改查的基础service
 * @author:xiayingjie
 * @createTime:15-5-21 17:25
 */

public abstract class BaseService<T> {

    protected abstract BaseDAO getDAO();

    /**
     * 查询所有的对象
     *
     * @return
     */
    public List<T> findAll() {
        return (List<T>) getDAO().findAll();
    }

    /**
     * 条件分页查询相关结果
     *
     * @param specification
     * @param pageRequest
     * @return
     */
    public Page<T> findAll(Specification specification, PageRequest pageRequest) {
        return getDAO().findAll(specification, pageRequest);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public void deleteById(int id) {
        getDAO().delete(id);
    }

    /**
     * 根据id查找对象
     *
     * @param id
     * @return
     */
    public T findById(int id) {
        return (T) getDAO().findOne(id);
    }

    /**
     * 增加或修改
     *
     * @param object
     * @return
     */
    public void save(T object) {
        getDAO().save(object);
    }
}
