package com.digital.receipt.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.digital.receipt.common.enums.WebRole;

/**
 * Annotation for checking if user has access to an endpoint.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasAccess {
    WebRole value() default WebRole.USER;
}
