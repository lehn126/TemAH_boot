package com.temah.common.web;

import com.temah.common.page.PagingResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IBaseService<T, ID extends Serializable> {

    int create(T entity);

    int batchCreate(List<T> list);

    int update(T entity) throws Exception;

    int delete(ID id);

    int delete(List<ID> ids);

    T findById(ID id);

    PagingResult findByCriteria(Map<String, Object> params);
}
