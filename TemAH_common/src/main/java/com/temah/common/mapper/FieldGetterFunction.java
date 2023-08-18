package com.temah.common.mapper;

@FunctionalInterface
public interface FieldGetterFunction<O, F> {
    F apply(O source);

}
