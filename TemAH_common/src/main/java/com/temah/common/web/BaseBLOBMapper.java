package com.temah.common.web;

import java.io.Serializable;

public interface BaseBLOBMapper<T, ID extends Serializable> extends BaseMapper<T, ID> {

    int updateByPrimaryKeyWithBLOBs(T row);

}