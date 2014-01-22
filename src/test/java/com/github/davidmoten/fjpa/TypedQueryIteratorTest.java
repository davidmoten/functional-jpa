package com.github.davidmoten.fjpa;

import javax.persistence.TypedQuery;

import org.junit.Test;

public class TypedQueryIteratorTest {

	@Test
	public void test() {
		TypedQuery<String> q = null;
		TypedQueryIterator.query(q).pageSize(100).iterator();
		TypedQueryIterator.query(q).pageSize(100).fluentIterator();
	}

}
