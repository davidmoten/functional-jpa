package com.github.davidmoten.fjpa;

import static com.google.common.base.Optional.of;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.FluentIterable;

class TypedQueryIterator<T> extends AbstractIterator<T> {

	private final TypedQuery<T> query;

	private Optional<Iterator<T>> it = Optional.absent();
	private int position = 0;

	private final int pageSize;

	private TypedQueryIterator(TypedQuery<T> query, int pageSize) {
		Preconditions.checkNotNull(query);
		this.query = query;
		this.pageSize = pageSize;
	}

	@Override
	protected T computeNext() {
		if (!it.isPresent() || !it.get().hasNext()) {
			it = of(query.setFirstResult(position).setMaxResults(pageSize)
					.getResultList().iterator());
			if (!it.get().hasNext())
				return endOfData();
		}
		position++;
		return it.get().next();
	}

	public static class Builder<T> implements Iterable<T> {
		private final TypedQuery<T> query;
		private int pageSize = 100;

		public Builder(TypedQuery<T> query) {
			this.query = query;
		}

		public Builder<T> pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		@Override
		public Iterator<T> iterator() {
			return new TypedQueryIterator<T>(query, pageSize);
		}

		public TypedQuery<T> get() {
			return query;
		}

		public FluentIterable<T> fluent() {
			return FluentIterable.from(this);
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

		public <R> Parameter<R> getParameter(String arg0, Class<R> arg1) {
			return query.getParameter(arg0, arg1);
		}

		public <R> Parameter<R> getParameter(int arg0, Class<R> arg1) {
			return query.getParameter(arg0, arg1);
		}

		public <R> R getParameterValue(Parameter<R> arg0) {
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

		public <R> R unwrap(Class<R> arg0) {
			return query.unwrap(arg0);
		}

		public FluentIterable<T> getResultList() {
			return FluentIterable.from(query.getResultList());
		}

		public T getSingleResult() {
			return query.getSingleResult();
		}

		public Builder<T> firstResult(int arg0) {
			query.setFirstResult(0);
			return this;
		}

		public Builder<T> flushMode(FlushModeType arg0) {
			query.setFlushMode(arg0);
			return this;
		}

		public Builder<T> hint(String arg0, Object arg1) {
			query.setHint(arg0, arg1);
			return this;
		}

		public Builder<T> lockMode(LockModeType arg0) {
			query.setLockMode(arg0);
			return this;
		}

		public Builder<T> maxResults(int arg0) {
			query.setMaxResults(arg0);
			return this;
		}

		public Builder<T> parameter(Parameter<T> arg0, T arg1) {
			query.setParameter(arg0, arg1);
			return this;
		}

		public Builder<T> parameter(String arg0, Object arg1) {
			query.setParameter(arg0, arg1);
			return this;
		}

		public Builder<T> parameter(int arg0, Object arg1) {
			query.setParameter(arg0, arg1);
			return this;
		}

		public Builder<T> parameter(Parameter<Calendar> arg0, Calendar arg1,
				TemporalType arg2) {
			query.setParameter(arg0, arg1, arg2);
			return this;
		}

		public Builder<T> parameter(Parameter<Date> arg0, Date arg1,
				TemporalType arg2) {
			query.setParameter(arg0, arg1);
			return this;
		}

		public Builder<T> parameter(String arg0, Calendar arg1,
				TemporalType arg2) {
			query.setParameter(arg0, arg1, arg2);
			return this;
		}

		public Builder<T> parameter(String arg0, Date arg1, TemporalType arg2) {
			query.setParameter(arg0, arg1, arg2);
			return this;
		}

		public Builder<T> parameter(int arg0, Calendar arg1, TemporalType arg2) {
			query.setParameter(arg0, arg1, arg2);
			return this;
		}

		public Builder<T> parameter(int arg0, Date arg1, TemporalType arg2) {
			query.setParameter(arg0, arg1, arg2);
			return this;
		}

	}

}
