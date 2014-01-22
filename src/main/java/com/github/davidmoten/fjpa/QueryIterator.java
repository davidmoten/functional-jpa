package com.github.davidmoten.fjpa;

import static com.google.common.base.Optional.of;

import java.util.Iterator;

import javax.persistence.Query;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.FluentIterable;

public class QueryIterator<T> extends AbstractIterator<T> {

	private final Query query;

	private Optional<Iterator<T>> it = Optional.absent();
	private int position = 0;

	private final int pageSize;

	private QueryIterator(Query query, int pageSize, Class<T> cls) {
		Preconditions.checkNotNull(query);
		this.query = query;
		this.pageSize = pageSize;
	}

	@Override
	protected T computeNext() {
		if (!it.isPresent() || !it.get().hasNext()) {
			it = of((Iterator<T>) query.setFirstResult(position)
					.setMaxResults(pageSize).getResultList().iterator());
			if (!it.get().hasNext())
				return endOfData();
		}
		position++;
		return it.get().next();
	}

	static <R> Builder<R> query(Query query,Class<R> cls) {
		return new Builder<R>(query,cls);
	}

	public static class Builder<R> implements Iterable<R> {
		private final Query query;
		private final Class<R> cls;
		private int pageSize = 100;

		public Builder(Query query, Class<R> cls) {
			this.query = query;
			this.cls = cls;
		}

		public Builder<R> pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		@Override
		public Iterator<R> iterator() {
			return new QueryIterator<R>(query, pageSize,cls);
		}

		public FluentIterable<R> fluent() {
			return FluentIterable.from(this);
		}
	}
}
