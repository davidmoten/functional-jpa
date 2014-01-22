package com.github.davidmoten.fjpa;

import static com.github.davidmoten.fjpa.Document.toId;
import static com.github.davidmoten.fjpa.TypedQueryIterator.query;
import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TypedQueryIteratorTest {

	@Test
	public void testIteratorReturnsAllDocuments() {
		EntityManagerFactory emf = emf();
		EntityManager em = emf.createEntityManager();
		insertDocuments(em);
		TypedQuery<Document> q = em.createQuery("from Document order by id",
				Document.class);
		assertEquals(Lists.newArrayList("a", "b", "c"), query(q).fluent().transform(toId()).toList());
		emf.close();
	}
	
	
	@Test
	public void testIteratorReturnsPartialListOfDocuments() {
		EntityManagerFactory emf = emf();
		EntityManager em = emf.createEntityManager();
		insertDocuments(em);
		TypedQuery<Document> q = em.createQuery("from Document where id > 'a' order by id",
				Document.class);
		assertEquals(Lists.newArrayList("b", "c"), query(q).fluent().transform(toId()).toList());
		emf.close();
	}
	
	@Test
	public void testIteratorReturnsNoDocuments() {
		EntityManagerFactory emf = emf();
		EntityManager em = emf.createEntityManager();
		insertDocuments(em);
		TypedQuery<Document> q = em.createQuery("from Document where id > 'c' order by id",
				Document.class);
		assertEquals(Lists.newArrayList(), query(q).fluent().transform(toId()).toList());
		emf.close();
	}
	
	@Test
	public void testIteratorReturnsAllDocumentsUsingTwoPages() {
		EntityManagerFactory emf = emf();
		EntityManager em = emf.createEntityManager();
		insertDocuments(em);
		TypedQuery<Document> q = em.createQuery("from Document order by id",
				Document.class);
		assertEquals(Lists.newArrayList("a", "b", "c"), query(q).pageSize(2).fluent().transform(toId()).toList());
		emf.close();
	}
	
	@Test
	public void testIteratorReturnsAllDocumentsUsingThreePages() {
		EntityManagerFactory emf = emf();
		EntityManager em = emf.createEntityManager();
		insertDocuments(em);
		TypedQuery<Document> q = em.createQuery("from Document order by id",
				Document.class);
		assertEquals(Lists.newArrayList("a", "b", "c"), query(q).pageSize(1).fluent().transform(toId()).toList());
		emf.close();
	}
	

	private void insertDocuments(EntityManager em) {
		em.getTransaction().begin();
		em.persist(new Document("a"));
		em.persist(new Document("b"));
		em.persist(new Document("c"));
		em.getTransaction().commit();
	}

	private EntityManagerFactory emf() {
		return Persistence.createEntityManagerFactory("test");
	}

}
