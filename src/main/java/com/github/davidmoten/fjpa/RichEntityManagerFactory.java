package com.github.davidmoten.fjpa;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Optional.of;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class RichEntityManagerFactory {

	private static org.slf4j.Logger log = LoggerFactory
			.getLogger(RichEntityManagerFactory.class);

	private final EntityManagerFactory emf;

	public RichEntityManagerFactory(EntityManagerFactory emf) {
		Preconditions.checkNotNull(emf);
		this.emf = emf;
	}

	public RichEntityManager createEntityManager() {
		return new RichEntityManager(emf.createEntityManager());
	}

	public RichEntityManager createEntityManager(Map<String, Object> map) {
		return new RichEntityManager(emf.createEntityManager(map));
	}

	public RichEntityManager em() {
		return createEntityManager();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return emf.getCriteriaBuilder();
	}

	public Metamodel getMetamodel() {
		return emf.getMetamodel();
	}

	public boolean isOpen() {
		return emf.isOpen();
	}

	public RichEntityManagerFactory close() {
		emf.close();
		return this;
	}

	public Map<String, Object> getProperties() {
		return emf.getProperties();
	}

	public Cache getCache() {
		return emf.getCache();
	}

	public PersistenceUnitUtil getPersistenceUnitUtil() {
		return emf.getPersistenceUnitUtil();
	}

	/**
	 * Runs an entity manager task within a transaction and closes the
	 * transaction and EntityManager safely. If the transaction fails an error
	 * is logged via log4j and the exception is rethrown if and only if st
	 * rategy is THROW_EXCEPTION. If strategy is THROW_AND_LOG_EXCEPTION then
	 * the exception is logged and the exception is rethrown. If the strategy is
	 * LOG_EXCEPTION then the exception is logged and Optional.absent()
	 * returned.
	 * 
	 * @param emf
	 * @param task
	 * @param rethrowException
	 * @return
	 */
	public <T> TaskOptionalResult<T> run(Task<T> task, boolean throwException,
			boolean logException) {
		Optional<RichEntityManager> em = Optional.absent();
		try {
			em = of(createEntityManager().begin());
			T t = em.get().run(task);
			em.get().commit();
			return new TaskOptionalResult<T>(fromNullable(t), this);
		} catch (RuntimeException e) {
			rollback(em);
			if (logException)
				log.error(e.getMessage(), e);
			if (throwException)
				throw e;
			else
				return new TaskOptionalResult<T>(Optional.<T> absent(), this);
		} finally {
			close(em);
		}
	}

	@VisibleForTesting
	static void close(Optional<RichEntityManager> em) {
		Preconditions.checkNotNull(em);
		if (em.isPresent() && em.get().isOpen())
			em.get().close();
	}

	@VisibleForTesting
	static void rollback(Optional<RichEntityManager> em) {
		Preconditions.checkNotNull(em);
		if (em.isPresent() && em.get().getTransaction().isActive())
			em.get().rollback();
	}

	public <T> TaskResult<T> run(Task<T> task) {
		return new TaskResult<T>(run(task, true, true).result().get(), this);
	}

	public <T> TaskResult<T> runWithoutLoggingError(Task<T> task) {
		return new TaskResult<T>(run(task, true, false).result().get(), this);
	}

	public <T> TaskOptionalResult<T> runNoLogNoThrow(Task<T> task) {
		return run(task, false, false);
	}

	public RichEntityManagerFactory run(final TaskVoid task,
			boolean throwException, boolean logError) {
		run(new Task<Void>() {
			@Override
			public Void run(RichEntityManager em) {
				task.run(em);
				return null;
			}
		}, throwException, logError);
		return this;
	}

	public RichEntityManagerFactory run(TaskVoid task) {
		return run(task, true, true);
	}

	public RichEntityManagerFactory runScript(InputStream is, Charset charset) {
		final StringBuffer s = readString(is, charset);
		String[] items = s.toString().split(";");
		List<String> commands = from(newArrayList(items)).filter(notNull())
				.toList();
		run(commands);
		return this;
	}

	public void run(final List<String> commands) {
		run(new TaskVoid() {
			@Override
			public void run(RichEntityManager em) {
				for (String command : commands) {
					log.info(command.trim());
					if (command.trim().length() > 0)
						em.get().createNativeQuery(command.trim())
								.executeUpdate();
				}
			}
		});
	}

	private static StringBuffer readString(InputStream is, Charset charset) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is,
				charset));
		return readString(br);
	}

	@VisibleForTesting
	static StringBuffer readString(BufferedReader br) {
		final StringBuffer s = new StringBuffer();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				if (s.length() > 0)
					s.append('\n');
				s.append(line);
			}
			br.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return s;
	}
}
