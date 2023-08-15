package com.temah.common.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T, ID extends Serializable> {
    /**
     * 由 mybatis-generator 产生
     */
    int deleteByPrimaryKey(ID id);

    int deleteByPrimaryKeys(List<ID> ids);

    /**
     * 由 mybatis-generator 产生
     */
    int insert(T record);

    /**
     * 由 mybatis-generator 产生
     */
    int insertSelective(T record);

    int batchInsert(List<T> list);

    /**
     * 由 mybatis-generator 产生
     */
    T selectByPrimaryKey(ID id);

    /**
     * 由 mybatis-generator 产生
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * 由 mybatis-generator 产生
     */
    int updateByPrimaryKey(T record);

    Long countByCriteria(Map<String, Object> params);

    List<Map<String, Object>> selectByCriteriaWithPage(Map<String, Object> params);
}
