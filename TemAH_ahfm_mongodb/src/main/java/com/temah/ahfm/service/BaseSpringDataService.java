package com.temah.ahfm.service;

import com.temah.common.page.PagingResult;
import com.temah.common.web.IBaseService;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class BaseSpringDataService<T, ID extends Serializable> implements IBaseService<T, ID> {
    protected MongoRepository<T, ID> repository;

    public BaseSpringDataService(MongoRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = false)
    public T save(T entity) {
        return repository.save(entity);
    }

    @Transactional(readOnly = false)
    public List<T> saveAll(Iterable<T> entities) {
        return repository.saveAll(entities);
    }

    @Transactional(readOnly = true)
    public T findById(ID id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = false)
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Transactional(readOnly = false)
    public void deleteAllById(Iterable<ID> ids) {
        repository.deleteAllById(ids);
    }

    @Transactional(readOnly = false)
    public void deleteAll(Iterable<T> entities) {
        repository.deleteAll(entities);
    }

    @Transactional(readOnly = false)
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = false)
    public T insert(T entity) {
        return repository.insert(entity);
    }

    @Transactional(readOnly = false)
    public List<T> insert(Iterable<T> entities) {
        return repository.insert(entities);
    }

    @Transactional(readOnly = true)
    public Optional<T> findOne(Example<T> example) {
        return repository.findOne(example);
    }

    @Transactional(readOnly = true)
    public List<T> findAll(Example<T> example) {
        return repository.findAll(example);
    }

    @Transactional(readOnly = true)
    public List<T> findAll(Example<T> example, Sort sort) {
        return repository.findAll(example, sort);
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Example<T> example, Pageable pageable) {
        return repository.findAll(example, pageable);
    }

    @Transactional(readOnly = true)
    public long count(Example<T> example) {
        return repository.count(example);
    }

    @Transactional(readOnly = true)
    public boolean exists(Example<T> example) {
        return repository.exists(example);
    }

    @Transactional(readOnly = true)
    public <R> R findBy(Example<T> example, Function<FluentQuery.FetchableFluentQuery<T>, R> queryFunction) {
        return repository.findBy(example, queryFunction);
    }

    // Custom methods
    //========================================================================
    @Transactional(readOnly = false)
    public int create(T entity) {
        return insert(entity) != null ? 1 : 0;
    }

    @Transactional(readOnly = false)
    public int batchCreate(List<T> list) {
        List<T> ret = insert(list);
        return ret != null ? ret.size() : 0;
    }

    @Transactional(readOnly = false)
    public int update(T entity) throws Exception {
        return save(entity) != null ? 1 : 0;
    }

    @Transactional(readOnly = false)
    public int delete(ID id) {
        deleteById(id);
        return 1;
    }

    @Transactional(readOnly = false)
    public int delete(List<ID> ids) {
        deleteAllById(ids);
        return ids != null ? ids.size() : 0;
    }

    @Transactional(readOnly = true)
    public T findById(ID id, T defaultValue) {
        return repository.findById(id).orElse(defaultValue);
    }

    @Transactional(readOnly = true)
    public PagingResult findByCriteria(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        if (!params.containsKey("pageIndex")) {
            params.put("pageIndex", 1);
        }
        if (!params.containsKey("pageSize")) {
            params.put("pageSize", Integer.MAX_VALUE);
        }

        Sort sort = null;
        String sortBy = (String) params.get("sortBy");
        if (sortBy != null && !sortBy.isEmpty()) {
            String sortOrder = (String) params.get("sortOrder");
            sortOrder = (sortOrder != null && !sortOrder.isEmpty()) ? sortOrder : "asc";
            if ("desc".equalsIgnoreCase(sortOrder)) {
                sort = Sort.by(Sort.Order.desc(sortBy));
            } else {
                sort = Sort.by(Sort.Order.asc(sortBy));
            }
        }

        // Pageable 里 page 从 0 开始
        Pageable pageable = PageRequest.of((int) params.get("pageIndex") - 1,
                (int) params.get("pageSize"),
                sort != null ? sort : Sort.unsorted());

        // 这里是严格匹配，根据需要修改为模糊匹配
        Example<T> example = null;
        if (params.containsKey("_parameter")) {
            T probe = (T) params.get("_parameter");
            if (probe != null) {
                example = Example.of(probe, ExampleMatcher.matchingAll());
            }
        }

        Page<T> page = null;
        PagingResult pagingResult = new PagingResult(pageable.getPageNumber() + 1, pageable.getPageSize());
        if (example != null) {
            page = findAll(example, pageable);
        } else {
            page = findAll(pageable);
        }

        if (page != null) {
            pagingResult.setMaxCount(page.getTotalElements());
            pagingResult.setMaxPage(page.getTotalPages());
            pagingResult.setPageData(page.getContent());
        }

        return pagingResult;
    }
}
