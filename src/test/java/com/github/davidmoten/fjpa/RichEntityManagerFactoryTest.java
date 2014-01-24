package com.github.davidmoten.fjpa;

import static com.github.davidmoten.fjpa.Document.toId;
import static com.github.davidmoten.fjpa.EntityManagers.emf;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class RichEntityManagerFactoryTest {

	@Test
	public void testRichEmf() {
		RichEntityManager em = emf("test").em();
		ImmutableList<String> list = em.begin().persist(new Document("a"))
				.persist(new Document("b")).persist(new Document("c")).commit()
				.createQuery("from Document order by id", Document.class)
				.fluent().transform(toId()).toList();
		assertEquals(newArrayList("a", "b", "c"), list);
		em.closeAll();
	}

	@Test
	public void testRichEmfRun() {
		EntityManagers
		.emf("test")
		.run(new TaskVoid() {
			@Override
			public void run(RichEntityManager em) {
				ImmutableList<String> list = em
						.persist(new Document("a"))
						.persist(new Document("b"))
						.persist(new Document("c"))
						.createQuery("from Document order by id",
								Document.class).fluent().transform(toId())
						.toList();
				assertEquals(newArrayList("a", "b", "c"), list);
			}
		}).close();
	}

	@Test
	public void testRichEmfRunReturningList() {
		List<String> list = EntityManagers
				.emf("test")
				.run(new Task<List<String>>() {
					@Override
					public List<String> run(RichEntityManager em) {
						return em
								.persist(new Document("a"))
								.persist(new Document("b"))
								.persist(new Document("c"))
								.createQuery("from Document order by id",
										Document.class).fluent()
								.transform(toId()).toList();
					}
				});
		assertEquals(newArrayList("a", "b", "c"), list);
	}

}
