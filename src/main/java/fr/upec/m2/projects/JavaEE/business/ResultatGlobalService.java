package fr.upec.m2.projects.JavaEE.business;

import fr.upec.m2.projects.JavaEE.model.Resultat;
import fr.upec.m2.projects.JavaEE.model.Resultat_psd_1;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ramzi
 */
public class ResultatGlobalService implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(LoadDataService.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Resultat> getResultGlobal(String critereSelection) {
        switch (critereSelection) {
            case "1":
               return entityManager.createNamedQuery("Resultat_psd_1.getResultGlobal")
                     .getResultList();
            case "2":
                     return entityManager.createNamedQuery("Resultat_psd_2.getResultGlobal")
                     .getResultList();
            case "3":
                   return entityManager.createNamedQuery("Resultat_log_1.getResultGlobal")
                     .getResultList();
            case "4":
                   return entityManager.createNamedQuery("Resultat_log_2.getResultGlobal")
                     .getResultList();
        }   
        return null;
    }

    public List<Resultat> getWinnerByArr(String critereSelection) {
        StoredProcedureQuery query =
                entityManager.createStoredProcedureQuery("getwinnerByArr")
                        .registerStoredProcedureParameter("critereSelection",String.class, ParameterMode.IN).setParameter("critereSelection", critereSelection) ; 
        query.execute();
        return query.getResultList();
    }

    public List<String[]> getListCandidat() {
        return entityManager.createNamedQuery("Resultat_psd_1.getListCandidat")
                .getResultList();
    }
}