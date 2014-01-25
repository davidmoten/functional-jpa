package com.github.davidmoten.fjpa;

public interface Task<T> {
	T run(RichEntityManager em);
}
