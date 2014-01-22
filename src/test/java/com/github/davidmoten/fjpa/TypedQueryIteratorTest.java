package com.github.davidmoten.fjpa;

import static com.github.davidmoten.fjpa.Document.toId;
import static com.github.davidmoten.fjpa.Iterators.query;
import static com.github.davidmoten.fjpa.Util.emf;
import static com.github.davidmoten.fjpa.Util.insertDocuments;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.junit.Test;

public class TypedQueryIteratorTest {

	@Test
	public void testIteratorReturnsAllDocuments() {
		EntityManagerFactory emf = emf();
		EntityManager em = emf.createEntityManager();
		insertDocuments(em);
		TypedQuery<Document> q = em.createQuery("from Document order by id",
				Document.class);
		assertEquals(newArrayList("a", "b", "c"), query(q).fluent().transform(toId()).toList());
		emf.close();
	}
	
	
	@Test
	public void testIteratorReturnsPartialListOfDocuments() {
		EntityManagerFactory emf = emf();
		EntityManager em = emf.createEntityManager();
		insertDocuments(em);
		TypedQuery<Document> q = em.createQuery("from Document where id > 'a' order by id",
				Document.class);
		assertEquals(newArrayList("b", "c"), query(q).fluent().transform(toId()).toList());
		emf.close();
	}
	
	@Test
	public void testIteratorReturnsNoDocuments() {
		EntityManagerFactory emf = emf();
		EntityManager em = emf.createEntityManager();
		insertDocuments(em);
		TypedQuery<Document> q = em.createQuery("from Document where id > 'c' order by id",
				Document.class);
		assertEquals(newArrayList(), query(q).fluent().transform(toId()).toList());
		emf.close();
	}
	
	@Test
	public void testIteratorReturnsAllDocumentsUsingTwoPages() {
		EntityManagerFactory emf = emf();
		EntityManager em = emf.createEntityManager();
		insertDocuments(em);
		TypedQuery<Document> q = em.createQuery("from Document order by id",
				Document.class);
		assertEquals(newArrayList("a", "b", "c"), query(q).pageSize(2).fluent().transform(toId()).toList());
		emf.close();
	}
	
	@Test
	public void testIteratorReturnsAllDocumentsUsingThreePages() {
		EntityManagerFactory emf = emf();
		EntityManager em = emf.createEntityManager();
		insertDocuments(em);
		TypedQuery<Document> q = em.createQuery("from Document order by id",
				Document.class);
		assertEquals(newArrayList("a", "b", "c"), query(q).pageSize(1).fluent().transform(toId()).toList());
		emf.close();
	}
	

}
