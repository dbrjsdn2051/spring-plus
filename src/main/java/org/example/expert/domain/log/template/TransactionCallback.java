package org.example.expert.domain.log.template;

@FunctionalInterface
public interface TransactionCallback<T> {
    T execute() throws Exception;
}
