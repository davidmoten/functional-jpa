package com.github.davidmoten.fjpa;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Iterators {

	public static <R> QueryIterator.Builder<R> query(Query query, Class<R> cls) {
		return new QueryIterator.Builder<R>(query, cls);
	}

	public static <R> TypedQueryIterator.Builder<R> query(TypedQuery<R> query) {
		return new TypedQueryIterator.Builder<R>(query);
	}

}
