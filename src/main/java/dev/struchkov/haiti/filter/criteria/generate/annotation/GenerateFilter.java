package dev.struchkov.haiti.filter.criteria.generate.annotation;

import dev.struchkov.haiti.filter.criteria.generate.domain.FilterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface GenerateFilter {

    String name() default "";

    FilterType[] types() default FilterType.SINGLE;

}
