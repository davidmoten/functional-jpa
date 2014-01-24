package com.github.davidmoten.fjpa;

public class TaskResult<T> {
	private final T result;
	private final RichEntityManagerFactory emf;

	public TaskResult(T result, RichEntityManagerFactory emf) {
		super();
		this.result = result;
		this.emf = emf;
	}

	public T result() {
		return result;
	}

	public RichEntityManagerFactory emf() {
		return emf;
	}

}
