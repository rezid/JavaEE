package fr.upec.m2.projects.JavaEE.logic;

import fr.upec.m2.projects.JavaEE.data.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserDao {

    @PersistenceContext(unitName = "myPU")
    private EntityManager em;

    public void createUse(User user) {
        em.persist(user);
    }
}
