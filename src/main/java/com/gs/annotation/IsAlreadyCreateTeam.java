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
@Constraint(validatedBy = {IsAlreadyCreateTeam.IsAlreadyCreateTeamValidator.class})
@Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER})

/**
 * 自定义注解-玩家是否已经创建过战队
 * User: lys
 * DateTime: 2022-04-22
 **/
public @interface IsAlreadyCreateTeam {
    String message() default "该玩家已经创建过战队";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @AllArgsConstructor
    public class IsAlreadyCreateTeamValidator implements ConstraintValidator<IsAlreadyCreateTeam, Long> {

        private TeamRepository teamRepository;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {

            if (teamRepository.existsByCreateMemberId(value)) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        public void initialize(IsAlreadyCreateTeam constraintAnnotation) {

        }
    }
}
