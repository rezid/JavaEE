package fr.upec.m2.projects.JavaEE.business.service;
import fr.upec.m2.projects.JavaEE.model.Resultat;
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

   public List<Resultat> getResultByName(String nomCandidat, String prenomCandidat,String model) {
        String queryname =  model+".getResultByName";
                           
        return entityManager.createNamedQuery(queryname)
                .setParameter("nomC", nomCandidat)
                .setParameter("prenomC",prenomCandidat )
                .getResultList();
}
        public List<String[]> getAllResultByArrondissement(String nomCandidat, String prenomCandidat,String model) {
            String queryname =  model+".getAllResultByArrondissement";
            return entityManager.createNamedQuery(queryname)
                 .setParameter("nomC", nomCandidat)
                .setParameter("prenomC",prenomCandidat )
                .getResultList();
}
    
    
      public List<String[]> getListCandidat(String model) {       
          String queryname =  model+".getListCandidat";
        return entityManager.createNamedQuery(queryname)
                .getResultList();        
}
      public List<String[]> getStatistiqueByArrondissement() {       
        return entityManager.createNamedQuery("Resultat_psd_1.getStatistiqueByArrondissement")
                .getResultList();        
}
        public void updateData(long id,String numArr,String nom, String prenom, String numBurr, String nbrV,String nbrVoix, String nbrInscrits, String nbrExprime) {
                entityManager.createNamedQuery("Resultat_psd_1.updateData")
                .setParameter("id", id)
                .setParameter("numArr", numArr)
                .setParameter("nom", nom)
                .setParameter("prenom", prenom)
                .setParameter("numBurr", numBurr)
                .setParameter("nbrV", nbrV)
                  .setParameter("nbrVoix", nbrVoix)
                  .setParameter("nbrInscrit", nbrInscrits)
                  .setParameter("nbrExprime", nbrExprime).executeUpdate();
                
    }
     

}
