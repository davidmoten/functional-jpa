package com.github.davidmoten.fjpa;

import static com.github.davidmoten.fjpa.Document.toId;
import static com.github.davidmoten.fjpa.EntityManagers.emf;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
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
		RichEntityManagerFactory emf = EntityManagers.emf("test");
		emf.run(new TaskVoid() {
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
		RichEntityManagerFactory emf = EntityManagers.emf("test");
		List<String> list = emf.run(new Task<List<String>>() {
			@Override
			public List<String> run(RichEntityManager em) {
				return em
						.persist(new Document("a"))
						.persist(new Document("b"))
						.persist(new Document("c"))
						.createQuery("from Document order by id",
								Document.class).fluent().transform(toId())
						.toList();
			}
		}).result();
		assertEquals(newArrayList("a", "b", "c"), list);
		emf.close();
	}

	@Test
	public void testRichEmfRunReturningListAndUsingProcessor() {
		RichEntityManagerFactory emf = EntityManagers.emf("test");
		emf.run(new Task<List<String>>() {
			@Override
			public List<String> run(RichEntityManager em) {
				return em
						.persist(new Document("a"))
						.persist(new Document("b"))
						.persist(new Document("c"))
						.createQuery("from Document order by id",
								Document.class).fluent().transform(toId())
						.toList();
			}
		}).process(new Processor<List<String>>() {
			@Override
			public void process(List<String> list) {
				assertEquals(newArrayList("a", "b", "c"), list);
			}
		}).emf().close();
	}
	
	@Test
	public void testRunScript() {
		StringWriter commands = new StringWriter();
		commands.write("insert into document(id) values('a');\n");
		commands.write("insert into document(id) values('b');\n");
		InputStream is = new ByteArrayInputStream(commands.toString().getBytes());
		RichEntityManagerFactory emf = emf("test");
		long count = emf.runScript(is).em().count(Document.class);
		emf.close();
		assertEquals(2,count);
	}

}
