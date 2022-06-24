package com.gs.annotation;

import com.gs.repository.jpa.league.LeagueRepository;
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
@Constraint(validatedBy = {IsLeagueExist.IsLeagueExistValidator.class})
@Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER})


/**
 * 自定义注解-联盟是否存在
 * User: lys
 * DateTime: 2022-04-29
 **/
public @interface IsLeagueExist {
    String message() default "不存在该联盟";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @AllArgsConstructor
    public class IsLeagueExistValidator implements ConstraintValidator<IsLeagueExist, Long> {

        private LeagueRepository leagueRepository;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {

            if (leagueRepository.existsById(value)) {
                return true;
            }else {
                return false;
            }
        }

        @Override
        public void initialize(IsLeagueExist constraintAnnotation) {

        }
    }
}
