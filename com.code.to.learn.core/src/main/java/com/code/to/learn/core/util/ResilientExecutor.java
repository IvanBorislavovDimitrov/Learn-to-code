package com.code.to.learn.core.util;

import com.code.to.learn.core.constant.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Component
public class ResilientExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResilientExecutor.class);

    private static final int DEFAULT_RETRIES_COUNT = 3;
    private static final long TIME_BETWEEN_RETRIES = TimeUnit.SECONDS.toMillis(10);

    private final int retries;

    public ResilientExecutor() {
        this(DEFAULT_RETRIES_COUNT);
    }

    public ResilientExecutor(int retries) {
        this.retries = retries;
    }

    public <T> T executeWithRetry(SupplierWithCheckedException<T> supplier) {
        int retries = this.retries;
        while (true) {
            try {
                return supplier.execute();
            } catch (Exception e) {
                if (retries-- == 0) {
                    throw new RuntimeException(e.getMessage(), e);
                }
                sleep(TIME_BETWEEN_RETRIES);
                LOGGER.debug(MessageFormat.format(Messages.EXECUTION_RETRIED, retries, this.retries));
            }
        }
    }

    private void sleep(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @FunctionalInterface
    public interface SupplierWithCheckedException<E> {

        E execute() throws Exception;
    }
}
