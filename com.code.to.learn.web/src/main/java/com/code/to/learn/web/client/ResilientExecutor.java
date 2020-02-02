package com.code.to.learn.web.client;

import com.code.to.learn.web.message.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class ResilientExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResilientExecutor.class);

    private static final int DEFAULT_RETRIES_COUNT = 3;

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
                LOGGER.debug(MessageFormat.format(Messages.EXECUTION_RETRIED, retries, this.retries));
            }
        }
    }

    @FunctionalInterface
    public interface SupplierWithCheckedException<E> {

        E execute() throws Exception;
    }
}
