package fr.upec.m2.projects.JavaEE.business.service;

import fr.upec.m2.projects.JavaEE.model.Adresse;
import fr.upec.m2.projects.JavaEE.model.Point2D;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class AdresseService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Adresse> getAllAdresse() {
        return entityManager.createNamedQuery("Adresse.getAllAdresse")
                .getResultList();
    }

    public List<Point2D> getAllcoordinatesByAdresse(String adresse, String code_postal) {
        return entityManager.createNamedQuery("Adresse.getAllCoordinatesByAdresse")
                .setParameter("adresse", adresse)
                .setParameter("cp", code_postal)
                .getResultList();
    }
}