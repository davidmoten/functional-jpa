package com.github.davidmoten.fjpa;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TypedQueryIteratorTest {

	@Test
	public void testEmf() {
		EntityManager em = emf().createEntityManager();
		em.getTransaction().begin();
		em.persist(new Document("a"));
		em.persist(new Document("b"));
		em.persist(new Document("c"));
		em.getTransaction().commit();
		TypedQuery<Document> q = em.createQuery("from Document order by id",
				Document.class);
		assertEquals(3, TypedQueryIterator.query(q).fluent().size());
		assertEquals("a", TypedQueryIterator.query(q).iterator().next().id);
		assertEquals(Lists.newArrayList("a", "b", "c"), TypedQueryIterator
				.query(q).fluent().transform(Document.toId()).toList());
	}

	private EntityManagerFactory emf() {
		return Persistence.createEntityManagerFactory("test");
	}

}
