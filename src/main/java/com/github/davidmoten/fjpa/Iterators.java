package com.github.davidmoten.fjpa;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.google.common.annotations.VisibleForTesting;

public final class Iterators {

	private Iterators() {
		// prevent instantiation
	}

	@VisibleForTesting
	static void instantiateForTesting() {
		new Iterators();
	}

	public static <R> QueryIterator.Builder<R> query(Query query, Class<R> cls) {
		return new QueryIterator.Builder<R>(query, cls);
	}

	public static <R> TypedQueryIterator.Builder<R> query(TypedQuery<R> query) {
		return new TypedQueryIterator.Builder<R>(query);
	}

}
