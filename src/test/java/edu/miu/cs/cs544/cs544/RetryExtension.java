package edu.miu.cs.cs544.cs544;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class RetryExtension implements TestExecutionExceptionHandler {
    private static final int MAX_RETRIES = 3;

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        int failedAttempts = getFailedAttempts(context);
        if (failedAttempts < MAX_RETRIES) {
            storeFailedAttempts(context, failedAttempts);
            return;
        }
        throw throwable;
    }

    private int getFailedAttempts(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.GLOBAL).getOrComputeIfAbsent(context.getUniqueId(), key -> 0, Integer.class);
    }

    private void storeFailedAttempts(ExtensionContext context, int failedAttempts) {
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(context.getUniqueId(), failedAttempts + 1);
    }
}