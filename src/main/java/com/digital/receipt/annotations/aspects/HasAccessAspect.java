package com.digital.receipt.annotations.aspects;

import java.util.List;
import java.util.stream.Collectors;

import com.digital.receipt.annotations.interfaces.HasAccess;
import com.digital.receipt.common.exceptions.InsufficientPermissionsException;
import com.digital.receipt.jwt.utility.JwtHolder;
import com.google.common.collect.Lists;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Aspect to check if a user has access to the provided data.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Aspect
@Component
public class HasAccessAspect {

    @Autowired
    private JwtHolder jwtHolder;

    /**
     * Determines if the caller has access to this data.
     * 
     * @param joinPoint The args called with the method.
     * @param access    What type of access the method requires.
     * @return The arguements originally passed into the method.
     * @throws Throwable Exception if they don't have access
     */
    @Before("args(access)")
    @Around(value = "@annotation(anno)", argNames = "jp, anno")
    public Object access(ProceedingJoinPoint joinPoint, HasAccess access) throws Throwable {
        if (isClientCall(Thread.currentThread().getStackTrace()))
            return joinPoint.proceed();

        if (jwtHolder.getWebRole().getValue() > access.value().getValue()) {
            throw new InsufficientPermissionsException(
                    String.format("Insufficient Permissions for role '%s'!", jwtHolder.getWebRole()));
        }
        return joinPoint.proceed();
    }

    /**
     * Determines if the call was from a client method. If it was then the has
     * access is voided and it will continue the call.
     * 
     * @param elements Of the stack trace and calls
     * @return Boolean if it was a client class that called the method.
     */
    private boolean isClientCall(StackTraceElement[] elements) {
        List<StackTraceElement> stackElements = Lists.newArrayList(elements);
        return !stackElements.stream().filter(v -> v.getClassName().contains("com.digital.receipt.app"))
                .filter(v -> v.getClassName().contains("client")).collect(Collectors.toList()).isEmpty();
    }
}
