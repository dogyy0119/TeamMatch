package com.gs.annotation;

import com.gs.constant.enums.MemberJobEnum;
import lombok.AllArgsConstructor;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {JobCheck.JobCheckValidator.class})
@Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER})

/**
 * 自定义注解-战队成员的职务检查
 * User: lys
 * DateTime: 2022-04-29
 **/
public @interface JobCheck {
    String message() default "职务参数设置错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @AllArgsConstructor
    public class JobCheckValidator implements ConstraintValidator<JobCheck, Integer> {

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {

            if (!Objects.equals(value, MemberJobEnum.IS_TEAM_LEADER.getJob())
                    && !Objects.equals(value, MemberJobEnum.IS_TEAM_SECOND_LEADER.getJob())
                    && !Objects.equals(value, MemberJobEnum.IS_TEAM_MEMBER.getJob())) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        public void initialize(JobCheck constraintAnnotation) {

        }
    }
}
