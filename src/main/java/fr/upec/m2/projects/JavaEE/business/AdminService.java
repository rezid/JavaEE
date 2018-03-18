package fr.upec.m2.projects.JavaEE.business.service;

import fr.upec.m2.projects.JavaEE.model.AdminUser;
import fr.upec.m2.projects.JavaEE.model.Bureau;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;


@Stateless
public class AdminService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager entityManager;

    public Boolean getAdmin(String email, String password) {
        AdminUser admin=(AdminUser) entityManager.createNamedQuery("AdminUser.getAdmin")
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
        return admin!=null;
        
                
    }

    
}
