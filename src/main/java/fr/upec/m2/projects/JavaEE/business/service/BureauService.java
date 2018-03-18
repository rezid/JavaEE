package fr.upec.m2.projects.JavaEE.business.service;

import fr.upec.m2.projects.JavaEE.model.Bureau;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;


@Stateless
public class BureauService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Bureau> getAllBureau() {
        return entityManager.createNamedQuery("Bureau.getAllBureau")
                .getResultList();
    }

    public List<Bureau> getBurreauByCodePostale(String code_postale) {
        return entityManager.createNamedQuery("Bureau.getBurreauByArrondissement")
                .setParameter("code_postale", code_postale)
                .getResultList();
    }
    
     public void updateData(long id,String numBureau,String adresseBureau, String codePostale, String nomBureau) {
                entityManager.createNamedQuery("Bureau.updateData")
                .setParameter("id", id)
                .setParameter("numBureau", numBureau)
                .setParameter("adresse", adresseBureau)
                .setParameter("codePostal", codePostale)
                .setParameter("labelBureau", nomBureau).executeUpdate();
                
    }
      public List<String> getAllAdresse() {
        return entityManager.createNamedQuery("Bureau.getAllAdresse")
                .getResultList();
    }
}
