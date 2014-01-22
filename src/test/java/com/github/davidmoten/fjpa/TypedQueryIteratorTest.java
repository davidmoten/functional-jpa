package com.github.davidmoten.fjpa;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;

public class TypedQueryIteratorTest {

	@Test
	public void testEmf() {
		EntityManager em = emf().createEntityManager();
		em.getTransaction().begin();
		Document d = new Document("a");
		em.persist(d);
		em.getTransaction().commit();
		TypedQuery<Document> q = em.createQuery("from Document",
				Document.class);
		assertEquals("a", TypedQueryIterator.query(q).iterator().next().id);
	}

	private EntityManagerFactory emf() {
		return Persistence.createEntityManagerFactory("test");
	}

}
