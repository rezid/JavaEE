package fr.upec.m2.projects.JavaEE.annotation;

import java.lang.annotation.*;

@Documented
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.CLASS)
public @interface Trace {}