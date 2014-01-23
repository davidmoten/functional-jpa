package com.github.davidmoten.fjpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class Transactions {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(Transactions.class);

	/**
	 * Runs an entity manager task within a transaction and closes the
	 * transaction and EntityManager safely. If the transaction fails an error
	 * is logged via log4j and the exception is rethrown if and only if st
	 * rategy
	 * is THROW_EXCEPTION. If strategy is THROW_AND_LOG_EXCEPTION then the
	 * exception is logged and the exception is rethrown. If the strategy is
	 * LOG_EXCEPTION then the exception is logged and Optional.absent()
	 * returned.
	 * 
	 * @param emf
	 * @param task
	 * @param rethrowException
	 * @return
	 */
	public static <T> Optional<T> run(EntityManagerFactory emf, Task<T> task, 
			boolean throwException, boolean logException) {
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			em = emf.createEntityManager();
			tx = em.getTransaction();
			tx.begin();
			T t = task.run(em);
			tx.commit();
			return Optional.of(t);
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			if (logException)
				log.error(e.getMessage(), e);
			if (throwException)
				throw e;
			else
				return Optional.absent();
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	/**
	 * Runs an entity manager task within a transaction and closes the
	 * transaction and entitymanager safely. If the transaction fails an error
	 * is logged via log4j and the exception is rethrown .
	 * 
	 * @param emf
	 * @param task
	 * @return
	 */
	public static <T> T run(EntityManagerFactory emf, Task<T> task) {
		return run(emf, task, true, true).get();
	}

}
