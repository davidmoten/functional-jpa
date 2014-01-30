package com.github.davidmoten.fjpa;

import static com.github.davidmoten.fjpa.EntityManagers.emf;
import static com.google.common.collect.Lists.newArrayList;
import static org.funcito.FuncitoRxJava.callsTo;
import static org.funcito.FuncitoRxJava.func1For;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Observer;

public class ObservableTest {

	private static Logger log = LoggerFactory
			.getLogger(RichEntityManagerFactory.class);

	@Test
	public void testObservableRunningAsync() {
		final RichEntityManager em = emf("test").em();
		Observable<List<String>> observable = em.begin()
				.persist(new Document("a")).persist(new Document("b"))
				.persist(new Document("c")).commit()
				.createQuery("from Document order by id", Document.class)
				.observable().map(func1For(callsTo(Document.class).getId()))
				.toList();
		observable.subscribe(new Observer<List<String>>() {
			@Override
			public void onCompleted() {
				System.out.println("onCompleted");
				em.closeFactory();
			}

			@Override
			public void onError(Throwable e) {
				log.error(e.getMessage(), e);
				em.closeFactory();
			}

			@Override
			public void onNext(List<String> list) {
				assertEquals(newArrayList("a", "b", "c"), list);
				System.out.println("asserted ok ");
			}
		});
		log.info("finished 1");
	}
	
	@Test
	public void testObservableBlocking() {
		final RichEntityManager em = emf("test").em();
		List<String> list = em.begin()
				.persist(new Document("a")).persist(new Document("b"))
				.persist(new Document("c")).commit()
				.createQuery("from Document order by id", Document.class)
				.observable().map(func1For(callsTo(Document.class).getId()))
				.toList().toBlockingObservable().single();
		assertEquals(newArrayList("a", "b", "c"), list); 
		em.closeFactory();
		log.info("finished 2");
	}
}
