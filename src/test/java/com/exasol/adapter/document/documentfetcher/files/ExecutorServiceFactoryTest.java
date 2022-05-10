package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.concurrent.ExecutorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExecutorServiceFactoryTest {

    private ExecutorServiceFactory factory;

    @BeforeEach
    void setup() {
        factory = new ExecutorServiceFactory();
    }

    @Test
    void createsAnExecutorService() {
        final ExecutorService executorService = factory.getExecutorService();
        assertThat(executorService, notNullValue());
    }

    @Test
    void reusesTheSameExecutorService() {
        final ExecutorService executorService1 = factory.getExecutorService();
        final ExecutorService executorService2 = factory.getExecutorService();
        assertThat(executorService1, sameInstance(executorService2));
    }

    @Test
    void newExecutorServiceIsRunning() {
        final ExecutorService executorService = factory.getExecutorService();
        assertThat(executorService.isShutdown(), is(false));
        assertThat(executorService.isTerminated(), is(false));
    }

    @Test
    void closingFactoryClosesTheExecutor() {
        final ExecutorService executorService = factory.getExecutorService();
        factory.close();
        assertThat(executorService.isShutdown(), is(true));
        assertThat(executorService.isTerminated(), is(true));
    }

    @Test void closingFactoryWithoutExecutorServiceSucceeds(){
        assertDoesNotThrow(()->factory.close());
    }
}
