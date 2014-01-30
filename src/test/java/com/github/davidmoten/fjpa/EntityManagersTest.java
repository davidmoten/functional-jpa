package com.github.davidmoten.fjpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.collect.Maps;

public class EntityManagersTest {

	@Test
	public void getFullCoverage() {
		EntityManagers.instantiateForCoverage();
	}

	@Test
	public void testEnrichEntityManager() {
		EntityManager em = EasyMock.createMock(EntityManager.class);
		assertEquals(em, EntityManagers.enrich(em).get());
	}

	@Test
	public void testEnrichEntityManagerWithProperties() {
		Map<String, Object> map = Maps.newHashMap();
		assertNotNull(EntityManagers.emf("test", map));
	}
}
