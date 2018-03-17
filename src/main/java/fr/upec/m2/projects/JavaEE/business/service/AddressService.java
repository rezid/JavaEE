package fr.upec.m2.projects.JavaEE.business.service;

import fr.upec.m2.projects.JavaEE.model.Address;
import fr.upec.m2.projects.JavaEE.view.AddressBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class AddressService implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(AddressService.class);

    @PersistenceContext
    private EntityManager entityManager;

    public List<Address> searchByAddressPattern(String street_number_pattern, String street_name_pattern,
                                                String zip_code_pattern) {

        LOG.info("EJB method call searchByAddressPattern(-{}-, -{}-, -{}-)", street_number_pattern,
                street_name_pattern, zip_code_pattern);

        return entityManager.createNamedQuery("Address.searchByAddressPattern", Address.class)
                .setParameter("street_number_pattern", street_number_pattern)
                .setParameter("street_name_pattern", street_name_pattern)
                .setParameter("zip_code_pattern", zip_code_pattern)
                .getResultList();
    }
}
