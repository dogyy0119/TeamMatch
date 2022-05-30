package com.gs.annotation;

import com.gs.model.entity.jpa.db1.User;
import com.gs.repository.jpa.UserRepository;
import lombok.AllArgsConstructor;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 邮箱是否注册验证规则
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IsValidValue.IsEmailExistValidator.class})
@Target({ElementType.METHOD,
         ElementType.FIELD,
         ElementType.ANNOTATION_TYPE,
         ElementType.CONSTRUCTOR,
         ElementType.PARAMETER})
public @interface IsValidValue {

    String message() default "value is not correct!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @AllArgsConstructor
    public class IsEmailExistValidator implements ConstraintValidator<IsValidValue, Integer> {

        private final UserRepository userRepository;

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {

           if(value == 0 || value < 0) {
               return false;
           }
           return true;
        }

        @Override
        public void initialize(IsValidValue constraintAnnotation) {

        }
    }
}
