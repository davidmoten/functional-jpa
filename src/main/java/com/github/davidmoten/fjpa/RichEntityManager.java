package com.github.davidmoten.fjpa;

import static com.github.davidmoten.fjpa.Iterators.query;
import static com.google.common.base.Optional.fromNullable;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import com.github.davidmoten.fjpa.TypedQueryIterator.Builder;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class RichEntityManager {

    private final EntityManager em;

    public RichEntityManager(EntityManager em) {
        Preconditions.checkNotNull(em);
        this.em = em;
    }

    public EntityManager get() {
        return em;
    }

    public RichEntityManager persist(Object... entities) {
        for (Object entity : entities)
            em.persist(entity);
        return this;
    }

    public RichEntityManager begin() {
        em.getTransaction().begin();
        return this;
    }

    public RichEntityManager rollback() {
        em.getTransaction().rollback();
        return this;
    }

    public RichEntityManager commit() {
        em.getTransaction().commit();
        return this;
    }

    public <T> T run(Task<T> task) {
        return task.run(this);
    }

    public <T> T merge(T entity) {
        return em.merge(entity);
    }

    public RichEntityManager remove(Object entity) {
        em.remove(entity);
        return this;
    }

    public <T> TypedQueryIterator.Builder<T> findAll(Class<T> entityClass) {
        return createQuery("select from " + entityClass.getName(), entityClass);
    }

    public <T> long count(Class<T> entityClass) {
        return createQuery("select count(*) from " + entityClass.getName(), Long.class).fluent()
                .first().get();
    }

    public <T> Optional<T> find(Class<T> entityClass, Object primaryKey) {
        return fromNullable(em.find(entityClass, primaryKey));
    }

    public <T> Optional<T> find(Class<T> entityClass, Object primaryKey,
            Map<String, Object> properties) {
        return fromNullable(em.find(entityClass, primaryKey, properties));
    }

    public <T> Optional<T> find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        return fromNullable(em.find(entityClass, primaryKey, lockMode));
    }

    public <T> Optional<T> find(Class<T> entityClass, Object primaryKey, LockModeType lockMode,
            Map<String, Object> properties) {
        return fromNullable(em.find(entityClass, primaryKey, lockMode, properties));
    }

    public <T> Optional<T> getReference(Class<T> entityClass, Object primaryKey) {
        return fromNullable(em.getReference(entityClass, primaryKey));
    }

    public RichEntityManager flush() {
        em.flush();
        return this;
    }

    public RichEntityManager setFlushMode(FlushModeType flushMode) {
        em.setFlushMode(flushMode);
        return this;
    }

    public FlushModeType getFlushMode() {
        return em.getFlushMode();
    }

    public RichEntityManager lock(Object entity, LockModeType lockMode) {
        em.lock(entity, lockMode);
        return this;
    }

    public RichEntityManager lock(Object entity, LockModeType lockMode,
            Map<String, Object> properties) {
        em.lock(entity, lockMode, properties);
        return this;
    }

    public RichEntityManager refresh(Object entity) {
        em.refresh(entity);
        return this;
    }

    public RichEntityManager refresh(Object entity, Map<String, Object> properties) {
        em.refresh(entity, properties);
        return this;
    }

    public RichEntityManager refresh(Object entity, LockModeType lockMode) {
        em.refresh(entity, lockMode);
        return this;
    }

    public RichEntityManager refresh(Object entity, LockModeType lockMode,
            Map<String, Object> properties) {
        em.refresh(entity, lockMode, properties);
        return this;
    }

    public RichEntityManager clear() {
        em.clear();
        return this;
    }

    public RichEntityManager detach(Object entity) {
        em.detach(entity);
        return this;
    }

    public boolean contains(Object entity) {
        return em.contains(entity);
    }

    public LockModeType getLockMode(Object entity) {
        return em.getLockMode(entity);
    }

    public RichEntityManager setProperty(String propertyName, Object value) {
        em.setProperty(propertyName, value);
        return this;
    }

    public Map<String, Object> getProperties() {
        return em.getProperties();
    }

    public QueryIterator.Builder<Object> createQuery(String qlString) {
        return query(em.createQuery(qlString), Object.class);
    }

    public <T> TypedQueryIterator.Builder<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return query(em.createQuery(criteriaQuery));
    }

    public <T> TypedQueryIterator.Builder<T> createQuery(String qlString, Class<T> resultClass) {
        return query(em.createQuery(qlString, resultClass));
    }

    public QueryIterator.Builder<Object> createNamedQuery(String name) {
        return Iterators.query(em.createNamedQuery(name), Object.class);
    }

    public <T> Builder<T> createNamedQuery(String name, Class<T> resultClass) {
        return Iterators.query(em.createNamedQuery(name, resultClass));
    }

    public QueryIterator.Builder<Object> createNativeQuery(String sqlString) {
        return Iterators.query(em.createNativeQuery(sqlString), Object.class);
    }

    public <T> QueryIterator.Builder<T> createNativeQuery(String sqlString, Class<T> resultClass) {
        return query(em.createNativeQuery(sqlString, resultClass), resultClass);
    }

    public QueryIterator.Builder<Object> createNativeQuery(String sqlString, String resultSetMapping) {
        return query(em.createNativeQuery(sqlString, resultSetMapping), Object.class);
    }

    public RichEntityManager joinTransaction() {
        em.joinTransaction();
        return this;
    }

    public <T> T unwrap(Class<T> cls) {
        return em.unwrap(cls);
    }

    public Object getDelegate() {
        return em.getDelegate();
    }

    public RichEntityManager close() {
        em.close();
        return this;
    }

    public RichEntityManager closeFactory() {
        if (em.isOpen())
            em.close();
        if (em.getEntityManagerFactory().isOpen())
            em.getEntityManagerFactory().close();
        return this;
    }

    public boolean isOpen() {
        return em.isOpen();
    }

    public EntityTransaction getTransaction() {
        return em.getTransaction();
    }

    public RichEntityManagerFactory getEntityManagerFactory() {
        return new RichEntityManagerFactory(em.getEntityManagerFactory());
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }

    public Metamodel getMetamodel() {
        return em.getMetamodel();
    }

    public RichEntityManagerFactory emf() {
        return getEntityManagerFactory();
    }

}
