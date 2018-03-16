package fr.upec.m2.projects.JavaEE.business.service;

import fr.upec.m2.projects.JavaEE.model.Adresse;
import fr.upec.m2.projects.JavaEE.model.Point2D;
import fr.upec.m2.projects.JavaEE.model.Zone;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ZoneService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Zone> getAllZone() {
        return entityManager.createNamedQuery("Zone.getAllZone")
                .getResultList();
    }
}