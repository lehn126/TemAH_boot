package com.temah.common.mapper;

@FunctionalInterface
public interface FieldSetterFunction<R, F> {
    void apply(R result, F fieldValue);
}
