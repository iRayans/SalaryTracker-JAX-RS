package com.rayan.salarytracker.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * A utility class used to provide thread safe Entity Manager
 * as well as other JPA functionalities.
 */

public class JPAHelperUtil {
    private static EntityManagerFactory emf;

    // EntityManager is not thread-safe. It is designed to be used by a single thread at a time.
    // Each thread needs its own EntityManager to ensure that the database transactions for that request are isolated from others. 
    // ThreadLocal It ensures that each thread handling a request has its own EntityManager and doesn't share it with other threads.
    private static ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<>();

    private JPAHelperUtil() {
    }

    public static EntityManagerFactory getEntityMangerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("salarytrackerPU");
        }
        return emf;
    }

    public static EntityManager getEntityManager() {

        EntityManager em = threadLocalEntityManager.get();
        if (em == null || !em.isOpen()) {
            em = getEntityMangerFactory().createEntityManager();
            threadLocalEntityManager.set(em);
        }
        return em;
    }

    public static void closeEntityManager() {
        getEntityManager().close();
    }

    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }

    public static void commitTransaction() {
        getEntityManager().getTransaction().commit();
    }

    public static void rollbackTransaction() {
        getEntityManager().getTransaction().rollback();
    }

    public static void closeEntityManagerFactory() {
        emf.close();
    }
}