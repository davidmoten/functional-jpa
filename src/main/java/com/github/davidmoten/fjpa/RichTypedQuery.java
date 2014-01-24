package com.github.davidmoten.fjpa;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.google.common.collect.FluentIterable;

public class RichTypedQuery<T> {

	private final TypedQuery<T> query;

	public RichTypedQuery(TypedQuery<T> query) {
		this.query = query;
	}

	public int executeUpdate() {
		return query.executeUpdate();
	}

	public int getFirstResult() {
		return query.getFirstResult();
	}

	public FlushModeType getFlushMode() {
		return query.getFlushMode();
	}

	public Map<String, Object> getHints() {
		return query.getHints();
	}

	public LockModeType getLockMode() {
		return query.getLockMode();
	}

	public int getMaxResults() {
		return query.getMaxResults();
	}

	public Parameter<?> getParameter(String arg0) {
		return query.getParameter(arg0);
	}

	public Parameter<?> getParameter(int arg0) {
		return query.getParameter(arg0);
	}

	public <T> Parameter<T> getParameter(String arg0, Class<T> arg1) {
		return query.getParameter(arg0, arg1);
	}

	public <T> Parameter<T> getParameter(int arg0, Class<T> arg1) {
		return query.getParameter(arg0, arg1);
	}

	public <T> T getParameterValue(Parameter<T> arg0) {
		return query.getParameterValue(arg0);
	}

	public Object getParameterValue(String arg0) {
		return query.getParameterValue(arg0);
	}

	public Object getParameterValue(int arg0) {
		return query.getParameterValue(arg0);
	}

	public Set<Parameter<?>> getParameters() {
		return query.getParameters();
	}

	public boolean isBound(Parameter<?> arg0) {
		return query.isBound(arg0);
	}

	public <T> T unwrap(Class<T> arg0) {
		return query.unwrap(arg0);
	}

	public FluentIterable<T> getResultList() {
		return FluentIterable.from(query.getResultList());
	}

	public T getSingleResult() {
		return query.getSingleResult();
	}

	public TypedQuery<T> firstResult(int arg0) {
		return query.setFirstResult(0);
	}

	public TypedQuery<T> flushMode(FlushModeType arg0) {
		return query.setFlushMode(arg0);
	}

	public TypedQuery<T> hint(String arg0, Object arg1) {
		return query.setHint(arg0, arg1);
	}

	public TypedQuery<T> lockMode(LockModeType arg0) {
		return query.setLockMode(arg0);
	}

	public TypedQuery<T> maxResults(int arg0) {
		return query.setMaxResults(arg0);
	}

	public <T> TypedQuery<T> parameter(Parameter<T> arg0, T arg1) {
		return (TypedQuery<T>) query.setParameter(arg0, arg1);
	}

	public TypedQuery<T> setParameter(String arg0, Object arg1) {
		return query.setParameter(arg0, arg1);
	}

	public TypedQuery<T> parameter(int arg0, Object arg1) {
		return query.setParameter(arg0, arg1);
	}

	public TypedQuery<T> parameter(Parameter<Calendar> arg0, Calendar arg1,
			TemporalType arg2) {
		return query.setParameter(arg0, arg1, arg2);
	}

	public TypedQuery<T> parameter(Parameter<Date> arg0, Date arg1,
			TemporalType arg2) {
		return query.setParameter(arg0, arg1);
	}

	public TypedQuery<T> parameter(String arg0, Calendar arg1,
			TemporalType arg2) {
		return query.setParameter(arg0,arg1,arg2);
	}

	public TypedQuery<T> parameter(String arg0, Date arg1, TemporalType arg2) {
		return query.setParameter(arg0, arg1,arg2);
	}

	public TypedQuery<T> parameter(int arg0, Calendar arg1, TemporalType arg2) {
		return query.setParameter(arg0, arg1,arg2);
	}

	public TypedQuery<T> parameter(int arg0, Date arg1, TemporalType arg2) {
		return query.setParameter(arg0, arg1,arg2);
	}

}
