package com.github.davidmoten.fjpa;

import com.google.common.base.Optional;

public class TaskOptionalResult<T> {
    private final Optional<T> result;
    private final RichEntityManagerFactory emf;

    public TaskOptionalResult(Optional<T> result, RichEntityManagerFactory emf) {
        this.result = result;
        this.emf = emf;
    }

    public Optional<T> result() {
        return result;
    }

    public RichEntityManagerFactory emf() {
        return emf;
    }
}
