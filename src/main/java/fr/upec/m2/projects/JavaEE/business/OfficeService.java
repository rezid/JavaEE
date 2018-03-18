package fr.upec.m2.projects.JavaEE.business;

import fr.upec.m2.projects.JavaEE.model.Office;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;


@Stateless
public class OfficeService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Office> getAllOffice() {
        return entityManager.createNamedQuery("Office.getAllOffice", Office.class)
                .getResultList();
    }

    public List<Office> getAllOfficeByDistrict(String zip_code) {
        return entityManager.createNamedQuery("Office.getAllOfficeByDistrict", Office.class)
                .setParameter("zip_code", zip_code)
                .getResultList();
    }

    public List<Office> getOfficeByNumber(String office_number) {
        return entityManager.createNamedQuery("Office.getOfficeByNumber", Office.class)
                .setParameter("office_number", office_number)
                .getResultList();
    }
}
