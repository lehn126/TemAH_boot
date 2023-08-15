package com.temah.ahfm.mapper;

import com.temah.common.web.BaseMapper;

import java.io.Serializable;

public interface BaseBLOBFieldsMapper<T, ID extends Serializable> extends BaseMapper<T, ID> {

    int updateByPrimaryKeyWithBLOBs(T row);

}