package com.temah.common.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 使用源类型 O 的实例填充目标类型 R 的实例
 * @param <O> 来源类型
 * @param <R> 目标类型
 */
public class ObjectMapper<O, R> {
    private final List<FieldMapper<O, R, ?>> fieldMappers = new ArrayList<>();

    /**
     * 初始化属性mapper列表
     * @param fieldMappers
     */
    public void init(FieldMapper<O, R, ?>... fieldMappers) {
        if (fieldMappers != null && fieldMappers.length > 0) {
            this.fieldMappers.addAll(Arrays.asList(fieldMappers));
        }
    }

    /**
     * 使用源类型 O 的实例填充目标类型 R 的实例
     * @param source 源类型实例
     * @param result 目标类型实例
     * @return 目标类型实例
     */
    public R mapTo(O source, R result) {
        for (FieldMapper<O, R, ?> fieldMapper : fieldMappers) {
            fieldMapper.mapTo(source, result);
        }
        return result;
    }
}
