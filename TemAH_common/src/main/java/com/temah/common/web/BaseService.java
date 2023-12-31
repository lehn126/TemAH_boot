package com.temah.common.web;

import com.temah.common.constant.MsgConstant;
import com.temah.common.page.PagingResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseService <T, ID extends Serializable> implements IBaseService<T, ID> {

    protected BaseMapper<T, ID> mapper;

    public BaseService(BaseMapper<T, ID> mapper) {
        this.mapper = mapper;
    }

    @Transactional(readOnly = false)
    public int create(T entity) {
        return mapper.insertSelective(entity);
    }

    @Transactional(readOnly = false)
    public int batchCreate(List<T> list) {
        return mapper.batchInsert(list);
    }

    @Transactional(readOnly = false)
    public int update(T entity) throws Exception {
        int affectedRows = mapper.updateByPrimaryKeySelective(entity);
        if(affectedRows == 0) {
            throw new RuntimeException(MsgConstant.UPDATE_FAILED);
        }
        return affectedRows;
    }

    @Transactional(readOnly = false)
    public int delete(ID id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Transactional(readOnly = false)
    public int delete(List<ID> ids) {
        return mapper.deleteByPrimaryKeys(ids);
    }

    @Transactional(readOnly = true)
    public T findById(ID id){
        return mapper.selectByPrimaryKey(id);
    }

    @Transactional(readOnly = true)
    public PagingResult findByCriteria(Map<String, Object> params) {
        if(params == null) {
            params = new HashMap<>();
        }
        if(!params.containsKey("pageIndex")){
            params.put("pageIndex", 1);
        }
        if(!params.containsKey("pageSize")){
            params.put("pageSize", Integer.MAX_VALUE);
        }
        PagingResult pagingResult = new PagingResult((int) params.get("pageIndex"), (int) params.get("pageSize"));
        params.put("offset", pagingResult.getOffset());
        Long maxCount = mapper.countByCriteria(params);
        pagingResult.setMaxCount(maxCount);
        pagingResult.countMaxPage();
        if (maxCount > 0) {
            List<Map<String, Object>> list = mapper.selectByCriteriaWithPage(params);
            pagingResult.setPageData(list);
        }

        return pagingResult;
    }
}
