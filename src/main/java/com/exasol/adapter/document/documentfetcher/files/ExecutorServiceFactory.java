package com.exasol.adapter.document.documentfetcher.files;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ExecutorServiceFactory implements AutoCloseable{
    private ExecutorService service = null;

    /**
     * Get an {@link ExecutorService}.
     * 
     * @return {@link ExecutorService}
     */
    ExecutorService getExecutorService() {
        if (this.service == null) {
            this.service = Executors.newCachedThreadPool();
        }
        return this.service;
    }

    @Override
    public void close() {
        if (this.service != null) {
            this.service.shutdown();
        }
    }
}
