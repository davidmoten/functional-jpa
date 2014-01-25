package com.github.davidmoten.fjpa;

import static com.github.davidmoten.fjpa.Document.toId;
import static com.github.davidmoten.fjpa.EntityManagers.emf;
import static com.google.common.collect.Lists.newArrayList;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class RichEntityManagerFactoryTest {

	@Test
	public void testRichEmf() {
		RichEntityManager em = emf("test").em();
		ImmutableList<String> list = em.begin().persist(new Document("a"))
				.persist(new Document("b")).persist(new Document("c")).commit()
				.createQuery("from Document order by id", Document.class)
				.fluent().transform(toId).toList();
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
								Document.class).fluent().transform(toId)
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
								Document.class).fluent().transform(toId)
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
						.persist(new Document("a"), new Document("b"),
								new Document("c"))
						.createQuery("from Document order by id",
								Document.class).fluent().transform(toId)
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
		InputStream is = new ByteArrayInputStream(commands.toString()
				.getBytes());
		RichEntityManagerFactory emf = emf("test");
		long count = emf.runScript(is).em().count(Document.class);
		emf.close();
		assertEquals(2, count);
	}

	@Test
	public void testRunScriptWhneBlankCommand() {
		StringWriter commands = new StringWriter();
		commands.write("insert into document(id) values('a');\n");
		commands.write("      ;\n");
		commands.write("insert into document(id) values('b');\n");
		InputStream is = new ByteArrayInputStream(commands.toString()
				.getBytes());
		RichEntityManagerFactory emf = emf("test");
		long count = emf.runScript(is).em().count(Document.class);
		emf.close();
		assertEquals(2, count);
	}

	@Test
	public void testCreateEntityManager() {
		emf("test").createEntityManager(Maps.<String, Object> newHashMap())
				.begin().persist(new Document("a")).commit().close();
	}

	@Test
	public void testCreateCriteriaBuilder() {
		RichEntityManagerFactory emf = emf("test");
		assertNotNull(emf.getCriteriaBuilder());
		assertNotNull(emf.getMetamodel());
		assertTrue(emf.isOpen());
		assertNotNull(emf.getProperties());
		assertNotNull(emf.getCache());
		assertNotNull(emf.getPersistenceUnitUtil());
		assertNotNull(emf.getProperties());
		emf.close();
		assertFalse(emf.isOpen());
	}

	@Test(expected = RuntimeException.class)
	public void testTaskThrowsException() {
		emf("test").run(taskThrowingException());
	}

	@Test(expected = RuntimeException.class)
	public void testTaskThrowsExceptionNoLog() {
		emf("test").run(taskThrowingException(), true, false);
	}

	@Test
	public void testTaskThrowsExceptionNoLogNoThrow() {
		emf("test").run(taskThrowingException(), false, false);
	}

	@Test
	public void testTaskThrowsExceptionNoThrow() {
		emf("test").run(taskThrowingException(), false, true);
	}

	@Test
	public void testRollbackWhenInactive() {
		EntityTransaction tx = EasyMock.createMock(EntityTransaction.class);
		expect(tx.isActive()).andReturn(false);
		EasyMock.replay(tx);
		RichEntityManagerFactory.rollback(tx);
		EasyMock.verify(tx);
	}

	@Test
	public void testRollbackWhenActive() {
		EntityTransaction tx = EasyMock.createMock(EntityTransaction.class);
		expect(tx.isActive()).andReturn(true);
		tx.rollback();
		EasyMock.expectLastCall().once();
		EasyMock.replay(tx);
		RichEntityManagerFactory.rollback(tx);
		EasyMock.verify(tx);
	}

	@Test
	public void testRollbackOnNullTransaction() {
		RichEntityManagerFactory.rollback(null);
	}

	@Test
	public void testCloseEntityManager() {
		RichEntityManagerFactory.close(null);
	}

	@Test
	public void testCloseEntityManagerWhenOpen() {
		RichEntityManager em = EasyMock.createMock(RichEntityManager.class);
		expect(em.isOpen()).andReturn(true);
		expect(em.close()).andReturn(em).once();
		EasyMock.replay(em);
		RichEntityManagerFactory.close(em);
		EasyMock.verify(em);
	}

	@Test
	public void testCloseEntityManagerWhenClosed() {
		RichEntityManager em = EasyMock.createMock(RichEntityManager.class);
		expect(em.isOpen()).andReturn(false);
		EasyMock.replay(em);
		RichEntityManagerFactory.close(em);
		EasyMock.verify(em);
	}

	@Test(expected = RuntimeException.class)
	public void testReadStringWhenExceptionThrown() throws IOException {
		BufferedReader br = EasyMock.createMock(BufferedReader.class);
		EasyMock.expect(br.readLine()).andThrow(new IOException(""));
		EasyMock.replay(br);
		RichEntityManagerFactory.readString(br);
	}

	private TaskVoid taskThrowingException() {
		return new TaskVoid() {
			@Override
			public void run(RichEntityManager em) {
				throw new RuntimeException("test exception");
			}
		};
	}
}
