package com.github.davidmoten.fjpa;

import javax.persistence.EntityManager;

public interface Task<T> {
	T run(EntityManager em);
}
