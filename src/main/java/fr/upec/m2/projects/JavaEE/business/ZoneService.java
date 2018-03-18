package fr.upec.m2.projects.JavaEE.business;

import fr.upec.m2.projects.JavaEE.model.Zone;
import fr.upec.m2.projects.JavaEE.view.AddressBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ZoneService implements Serializable {
    private static final Logger LOG = LogManager.getLogger(ZoneService.class);
    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Zone> getAllZone() {
        List<Zone> zone_list = entityManager.createNamedQuery("Zone.getAllZone", Zone.class).getResultList();;
        LOG.info("EJB method call getAllZone(): {} zone(s) found!", zone_list.size());
        return  zone_list;
    }
}