package com.temah.common.mapper;

import java.util.function.Predicate;

public class FieldMapper<O, R, F> {
    /**
     * 属性名称
     */
    private final String fieldName;

    /**
     * validator用于检查来源类型实例是否可用
     */
    private Predicate<O> validator = new Predicate<O>() {
        @Override
        public boolean test(O o) {
            return true;
        }
    };

    /**
     * getter用于从来源类型实例中取出需要填充到目标类型属性的值
     */
    private FieldGetterFunction<O, F> getter;

    /**
     * setter用于将getter取出的值填充到目标类型实例的属性
     */
    private FieldSetterFunction<R, F> setter;

    /**
     * 初始化，省略validator
     *
     * @param fieldName 属性名称
     * @param getter    getter
     * @param setter    setter
     */
    public FieldMapper(String fieldName, FieldGetterFunction<O, F> getter,
                       FieldSetterFunction<R, F> setter) {
        this.fieldName = fieldName;
        this.getter = getter;
        this.setter = setter;
    }

    /**
     * 初始化
     *
     * @param fieldName 属性名称
     * @param getter    getter
     * @param validator validator
     * @param setter    setter
     */
    public FieldMapper(String fieldName, FieldGetterFunction<O, F> getter,
                       Predicate<O> validator, FieldSetterFunction<R, F> setter) {
        this.fieldName = fieldName;
        this.getter = getter;
        this.validator = validator;
        this.setter = setter;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Predicate<O> getValidator() {
        return validator;
    }

    public void setValidator(Predicate<O> validator) {
        this.validator = validator;
    }

    public FieldGetterFunction<O, F> getGetter() {
        return getter;
    }

    public void setGetter(FieldGetterFunction<O, F> getter) {
        this.getter = getter;
    }

    public FieldSetterFunction<R, F> getSetter() {
        return setter;
    }

    public void setSetter(FieldSetterFunction<R, F> setter) {
        this.setter = setter;
    }

    /**
     * 使用源类型 O 的实例填充目标类型 R 实例的属性
     *
     * @param source 源类型实例
     * @param result 目标类型实例
     */
    public void mapTo(O source, R result) {
        if (validator.test(source)) {
            F value = getter.apply(source);
            setter.apply(result, value);
        } else {
            throw new RuntimeException(String.format(
                    "Validate failed when mapper field '%s', source: %s",
                    fieldName,
                    source));
        }
    }
}
