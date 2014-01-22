package com.github.davidmoten.fjpa;

import static com.google.common.base.Optional.of;

import java.util.Iterator;

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
		
		public FluentIterable<T> fluent() {
			return FluentIterable.from(this);
		}
	}

}
