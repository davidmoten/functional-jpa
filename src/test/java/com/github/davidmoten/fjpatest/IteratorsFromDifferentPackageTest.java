package com.github.davidmoten.fjpatest;

import static com.github.davidmoten.fjpa.Document.toId;
import static com.github.davidmoten.fjpa.Iterators.query;
import static com.github.davidmoten.fjpa.TestingUtil.emf;
import static com.github.davidmoten.fjpa.TestingUtil.insertDocuments;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.github.davidmoten.fjpa.Document;

public class IteratorsFromDifferentPackageTest {

    @Test
    public void testTypedIteratorReturnsAllDocuments() {
        EntityManagerFactory emf = emf();
        EntityManager em = emf.createEntityManager();
        insertDocuments(em);
        TypedQuery<Document> q = em.createQuery("from Document order by id", Document.class);
        assertEquals(newArrayList("a", "b", "c"), query(q).fluent().transform(toId).toList());
        emf.close();
    }

    @Test
    public void testIteratorReturnsAllDocuments() {
        EntityManagerFactory emf = emf();
        EntityManager em = emf.createEntityManager();
        insertDocuments(em);
        Query q = em.createQuery("from Document order by id");
        assertEquals(newArrayList("a", "b", "c"), query(q, Document.class).fluent().transform(toId)
                .toList());
        emf.close();
    }

}
