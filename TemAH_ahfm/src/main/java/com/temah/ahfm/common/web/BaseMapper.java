package com.temah.ahfm.common.web;

import com.temah.ahfm.model.Alarm;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T, ID extends Serializable> {
    /**
     * 由 mybatis-generator 产生
     */
    int deleteByPrimaryKey(Integer id);

    int deleteByPrimaryKeys(@Param("ids") List<ID> ids);

    /**
     * 由 mybatis-generator 产生
     */
    int insert(T record);

    /**
     * 由 mybatis-generator 产生
     */
    int insertSelective(T record);

    int batchInsert(@Param("list") List<T> list);

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
