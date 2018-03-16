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

    public List<Bureau> getBurreauByNum(String numero_bureau) {
        return entityManager.createNamedQuery("Bureau.getBurreauByNum")
                .setParameter("num", numero_bureau)
                .getResultList();
    }
}
