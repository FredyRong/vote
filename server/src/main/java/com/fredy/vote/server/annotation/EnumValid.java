package com.fredy.vote.server.annotation;

import com.fredy.vote.server.utils.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 入参校验-枚举类型
 * @author Fredy
 * @date 2020-11-17 11:12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidator.class})
public @interface EnumValid {

    String message () default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 目标枚举类
     */
    Class<?> target() default Class.class;

    /**
     * 是否忽略空值
     */
    boolean ignoreEmpty() default false;
}
