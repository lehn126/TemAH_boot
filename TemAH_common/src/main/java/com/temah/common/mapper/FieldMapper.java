package com.temah.common.mapper;

import java.util.function.Predicate;

public class FieldMapper<O, R, F> {

    private final String fieldName;

    private Predicate<O> validator = new Predicate<O>() {
        @Override
        public boolean test(O o) {
            return true;
        }
    };

    private FieldGetterFunction<O, F> getter;

    private FieldSetterFunction<R, F> setter;

    public FieldMapper(String fieldName, FieldGetterFunction<O, F> getter,
                       FieldSetterFunction<R, F> setter) {
        this.fieldName = fieldName;
        this.getter = getter;
        this.setter = setter;
    }

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
