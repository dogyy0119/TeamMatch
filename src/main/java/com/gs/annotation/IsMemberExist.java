package com.gs.annotation;

import com.gs.repository.jpa.team.MemberRepository;
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
 * 自定义注解-玩家是否存在
 * User: lys
 * DateTime: 2022-04-29
 **/
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IsMemberExist.IsMemberExistValidator.class})
@Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER})

public @interface IsMemberExist {
    String message() default "不存在该玩家";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @AllArgsConstructor
    public class IsMemberExistValidator implements ConstraintValidator<IsMemberExist, Long> {

        private MemberRepository memberRepository;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {

            if (memberRepository.existsById(value)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void initialize(IsMemberExist constraintAnnotation) {

        }
    }
}
