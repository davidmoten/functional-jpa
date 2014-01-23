package com.github.davidmoten.fjpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestingUtil {
	public static  void insertDocuments(EntityManager em) {
		em.getTransaction().begin();
		em.persist(new Document("a"));
		em.persist(new Document("b"));
		em.persist(new Document("c"));
		em.getTransaction().commit();
	}

	public static EntityManagerFactory emf() {
		return Persistence.createEntityManagerFactory("test");
	}
}
