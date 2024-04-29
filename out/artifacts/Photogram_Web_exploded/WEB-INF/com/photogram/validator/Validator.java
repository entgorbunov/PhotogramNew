package com.photogram.validator;

public interface Validator<T> {

    ValidationResult isValid(T t);

}
