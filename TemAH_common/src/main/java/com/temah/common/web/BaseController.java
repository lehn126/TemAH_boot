package com.temah.common.web;

import java.io.Serializable;
import java.util.Map;

public class BaseController<T, ID extends Serializable> {

    protected BaseService<T, ID> service;

    public BaseController(BaseService<T, ID> service) {
        this.service = service;
    }

    protected void setPage(Map<String,Object> params, Integer pageIndex, Integer pageSize) {
        if(pageSize != null){
            params.put("pageSize", pageSize);
        }else{
            params.put("pageSize", Integer.MAX_VALUE);
        }
        if(pageIndex != null){
            params.put("pageIndex", pageIndex);
        }
    }

    protected void setPageWithSort(Map<String,Object> params,String sortBy,String sortOrder,Integer pageIndex,Integer pageSize) {
        if(sortBy != null && !sortBy.isEmpty()) {
            params.put("sortBy", sortBy);
        }
        if(sortOrder != null && !sortOrder.isEmpty()) {
            params.put("sortOrder", sortOrder);
        }
        if(pageSize != null){
            params.put("pageSize", pageSize);
        }else{
            params.put("pageSize", Integer.MAX_VALUE);
        }
        if(pageIndex != null){
            params.put("pageIndex", pageIndex);
        }
    }
}
