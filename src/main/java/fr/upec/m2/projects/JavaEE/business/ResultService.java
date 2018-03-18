package fr.upec.m2.projects.JavaEE.business;
import fr.upec.m2.projects.JavaEE.model.Resultat_psd_1;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ResultService  implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG  = LogManager.getLogger(LoadDataService.class);

    @PersistenceContext
    private EntityManager entityManager;

    public List<Resultat_psd_1> getResultByName(String nomCandidat, String prenomCandidat) {
        return entityManager.createNamedQuery("Resultat_psd_1.getResultByName")
                .setParameter("nomC", nomCandidat)
                .setParameter("prenomC",prenomCandidat )
                .getResultList();
}
        public List<String[]> getAllResultByArrondissement(String nomCandidat, String prenomCandidat) {
        return entityManager.createNamedQuery("Resultat_psd_1.getAllResultByArrondissement")
                 .setParameter("nomC", nomCandidat)
                .setParameter("prenomC",prenomCandidat )
                .getResultList();
}
    
    
      public List<String[]> getListCandidat() {       
        return entityManager.createNamedQuery("Resultat_psd_1.getListCandidat")
                .getResultList();        
}
      public List<String[]> getStatistiqueByArrondissement() {       
        return entityManager.createNamedQuery("Resultat_psd_1.getStatistiqueByArrondissement")
                .getResultList();        
}

}
