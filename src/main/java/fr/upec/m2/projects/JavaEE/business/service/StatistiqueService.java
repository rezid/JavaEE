package fr.upec.m2.projects.JavaEE.business.service;
import fr.upec.m2.projects.JavaEE.model.Resultat_psd_1;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ramzi
 */
public class StatistiqueService  implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG  = LogManager.getLogger(LoadDataService.class);

    @PersistenceContext
    private EntityManager entityManager;
      public List<String[]> getStatistiqueByArrondissement() {       
        return entityManager.createNamedQuery("Resultat_psd_1.getStatistiqueByArrondissement")
                .getResultList();        
}

        public List<String[]> getStatistiqueByBureaux() {       
        return entityManager.createNamedQuery("Resultat_psd_1.getStatistiqueByBureaux")
                .getResultList();        
}
}
