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
public class ResultatGlobalService  implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG  = LogManager.getLogger(LoadDataService.class);

    @PersistenceContext
    private EntityManager entityManager;

    public List<Resultat_psd_1> getResultGlobal() {
        return entityManager.createNamedQuery("Resultat_psd_1.getResultGlobal")
                .getResultList();
}
    
      public List<String[]> getListCandidat() {       
        return entityManager.createNamedQuery("Resultat_psd_1.getListCandidat")
                .getResultList();        
}

}
