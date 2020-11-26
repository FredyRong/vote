package com.fredy.vote.server.utils;

import com.fredy.vote.server.annotation.EnumValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Fredy
 * @date 2020-11-17 11:16
 */
public class EnumValidator implements ConstraintValidator<EnumValid, Integer> {

    // 枚举校验注解
    private EnumValid annotation;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = false;

        Class<?> cls = annotation.target();
        boolean ignoreEmpty = annotation.ignoreEmpty();

        if(cls.isEnum() && (value != null || !ignoreEmpty)) {
            Object[] objects = cls.getEnumConstants();
            for (Object object : objects) {
               if(object.toString().equals(String.valueOf(value))) {
                   result = true;
                   break;
               }
            }
        } else {
            result = true;
        }

        return result;
    }
}
