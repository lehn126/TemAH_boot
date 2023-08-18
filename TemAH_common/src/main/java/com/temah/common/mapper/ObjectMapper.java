package com.temah.common.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectMapper<O, R> {
    private final List<FieldMapper<O, R, ?>> fieldMappers = new ArrayList<>();

    public void init(FieldMapper<O, R, ?>... fieldMappers) {
        if (fieldMappers != null && fieldMappers.length > 0) {
            this.fieldMappers.addAll(Arrays.asList(fieldMappers));
        }
    }

    public R mapTo(O source, R result) {
        for (FieldMapper<O, R, ?> fieldMapper : fieldMappers) {
            fieldMapper.mapTo(source, result);
        }
        return result;
    }
}
