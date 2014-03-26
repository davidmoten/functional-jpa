package com.github.davidmoten.fjpa;

public interface Processor<T> {
    void process(T t);
}
