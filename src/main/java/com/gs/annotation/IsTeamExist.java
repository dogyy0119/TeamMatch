package com.gs.annotation;

import com.gs.repository.jpa.team.TeamRepository;
import lombok.AllArgsConstructor;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IsTeamExist.IsTeamExistValidator.class})
@Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER})


/**
 * 自定义注解-战队是否存在
 * User: lys
 * DateTime: 2022-04-29
 **/
public @interface IsTeamExist {
    String message() default "不存在战队";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @AllArgsConstructor
    public class IsTeamExistValidator implements ConstraintValidator<IsTeamExist, Long> {

        private TeamRepository teamRepository;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            if(value == null)
                return false;
            if (teamRepository.existsById(value)) {
                return true;
            }else {
                return false;
            }
        }

        @Override
        public void initialize(IsTeamExist constraintAnnotation) {

        }
    }
}
