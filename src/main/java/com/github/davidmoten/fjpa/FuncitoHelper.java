package com.github.davidmoten.fjpa;

import org.funcito.FuncitoGuava;

import com.google.common.base.Function;

public class FuncitoHelper {

	public static <T> T c(Class<T> cls) {
		return FuncitoGuava.callsTo(cls);
	}

	public static <T, V> Function<T, V> f(V v) {
		return (Function<T, V>) FuncitoGuava.functionFor(v);
	}

}
