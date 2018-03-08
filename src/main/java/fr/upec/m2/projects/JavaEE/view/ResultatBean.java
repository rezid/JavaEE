/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.upec.m2.projects.JavaEE.view;


import fr.upec.m2.projects.JavaEE.business.service.ResultService;
import fr.upec.m2.projects.JavaEE.model.Resultat_psd_1;
import fr.upec.m2.projects.JavaEE.view.utils.FilterList;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ViewScoped
@Named
public class ResultatBean implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = LogManager.getLogger(ResultatBean.class);
    
    private List<Resultat_psd_1> resultList;

    private String candidat;
    private FilterList filterList;
    private List <String[]> fullNameList ;
    private String[] nomPrenom;
   
            
    @Inject
    private ResultService resultService;

     @PostConstruct
    public void init() {
        resultList= resultService.getResultByName("SARKOZY", "Nicolas") ;
        fullNameList=resultService.getListCandidat();
        
    }

    public String getCandidat() {
        return candidat;
    }

    public void setCandidat(String candidat) {
        LOG.info("set candidat to: {}", candidat);
      
        this.candidat = candidat;
        
        nomPrenom = candidat.split(" ");
        //filterList.addFilter("Code_p", candidat);

            resultList = resultService.getResultByName(nomPrenom[0], nomPrenom[1]);

       
    }

   
 
    public List<Resultat_psd_1> getResultList() {
        return resultList;
    }

    public void setResultList(List<Resultat_psd_1> ResultList) {
        this.resultList = ResultList;
    }

    public List<String[]> getFullNameList() {
        return fullNameList;
    }

    public void setFullNameList(List<String[]> fullNameList) {
        this.fullNameList = fullNameList;
    }
  
    
    public int getListResultSize() {
        return resultList.size();
    }
    public int getListfullNameSize() {
        return fullNameList.size();
    }

}
