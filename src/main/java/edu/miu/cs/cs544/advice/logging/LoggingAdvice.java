package edu.miu.cs.cs544.advice.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAdvice {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvice.class);

    @Around("execution(* edu.miu.cs.cs544.controller.*.*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        return runLogger(joinPoint);
    }

    @AfterThrowing(pointcut = "execution(* edu.miu.cs.cs544.controller.*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.error("Exception in method {}: {}", methodName, ex.getMessage());
    }

    @Around("execution(* edu.miu.cs.cs544.service.*.*(..))")
    public Object logServiceMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        return runLogger(joinPoint);
    }

    private Object runLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] arguments = joinPoint.getArgs();


        logger.info("LoggingAdvice triggered for method: {}", methodName);
        logger.info("Arguments passed: {} "+methodName+" :" , Arrays.toString(arguments));
        logger.info("Entering method: {}", methodName);

        try {

            Long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            Long endTime = System.currentTimeMillis();
            long timeTaken = endTime-startTime;
            logger.info("Exiting method: {} - Time taken: {} ms",methodName,timeTaken);
            logger.info("Method execution results: "+((result != null)?result.toString():"null"));
            return result;
        } catch (Exception e) {
            logger.error("Exception in method {}: {}", methodName, e.getMessage());
            throw e;
        }
    }
}
