package com.rayan.salarytracker.dao;

import java.util.Optional;

import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.model.User;
import com.rayan.salarytracker.security.PasswordUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
@ApplicationScoped
public class UserDAO extends GenericCRUDImpl<User> implements IUserDAO {

    public UserDAO() {
        this.setPersistenceClass(User.class);
    }

    @Override
    public Optional<User> findUserByUserEmail(String email) {
        String jpql = "SELECT e FROM User e WHERE e.email = :email";
        try {
            EntityManager em = JPAHelperUtil.getEntityManager();
            TypedQuery<User> query = em.createQuery(jpql,User.class);
            query.setParameter("email", email);
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isUserValid(String email, String plainPassword) {
        String jpql = "SELECT e FROM User e WHERE e.email = :email";
        try {
            EntityManager em = JPAHelperUtil.getEntityManager();
            User user = em.createQuery(jpql,User.class)
            .setParameter("email", email)
            .getSingleResult();
            return PasswordUtil.checkPassword(plainPassword, user.getPassword());
            
        } catch (NoResultException e) {
            return false;
        }

    }

    @Override
    public boolean isEmailExists(String email) {
        String jpql = "SELECT COUNT(e) > 0 FROM User e WHERE e.email = :email";

        try {
            EntityManager em = JPAHelperUtil.getEntityManager();
            return em.createQuery(jpql,Boolean.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
    }
}
