package com.github.davidmoten.fjpa;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.common.annotations.VisibleForTesting;

/**
 * Utility methods for use with {@link EntityManager} and
 * {@link EntityManagerFactory}.
 */
public final class EntityManagers {

	private EntityManagers() {
		// prevent instantiation
	}

	@VisibleForTesting
	static void instantiateForCoverage() {
		new EntityManagers();
	}

	/**
	 * Returns an EntityManagerFactory with slight modifications to method
	 * signatures to enable method chaining and some useful additional methods.
	 * 
	 * @param emf
	 * @return
	 */
	public static RichEntityManagerFactory enrich(EntityManagerFactory emf) {
		return new RichEntityManagerFactory(emf);
	}

	/**
	 * Returns an EntityManager with slight modifications to method signatures
	 * to enable method chaining and some useful additional methods.
	 * 
	 * @param emf
	 * @return
	 */
	public static RichEntityManager enrich(EntityManager em) {
		return new RichEntityManager(em);
	}

	/**
	 * Returns an enriched EntityManagerFactory with slight modifications to
	 * method signatures to enable method chaining and some useful additional
	 * methods.
	 * 
	 * @param name
	 * @return
	 */
	public static RichEntityManagerFactory emf(String name) {
		return enrich(Persistence.createEntityManagerFactory(name));
	}

	/**
	 * Returns an enriched EntityManagerFactory with slight modifications to
	 * method signatures to enable method chaining and some useful additional
	 * methods.
	 * 
	 * @param name
	 * @param map
	 * @return
	 */
	public static RichEntityManagerFactory emf(String name,
			Map<String, Object> map) {
		return enrich(Persistence.createEntityManagerFactory(name, map));
	}

}
