/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.upec.m2.projects.JavaEE.view;


import fr.upec.m2.projects.JavaEE.business.ResultService;
import fr.upec.m2.projects.JavaEE.model.Resultat_psd_1;
import fr.upec.m2.projects.JavaEE.model.Resultat;
import fr.upec.m2.projects.JavaEE.model.Resultat_psd_2;
import fr.upec.m2.projects.JavaEE.model.Resultat_log_1;
import fr.upec.m2.projects.JavaEE.model.Resultat_log_2;

import fr.upec.m2.projects.JavaEE.view.utils.FilterList;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
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
    
    private List<Resultat> resultList;
  
    
    private String candidat;
    private FilterList filterList;
    private List <String[]> fullNameList ;
    private String[] nomPrenom;
    private List <String []> ResultByArrondissement ;
    private Integer scoreGlobal=0;
    private String[] s = new String[4];
    private String resultat_type;
    private boolean is_adr_asc = true;
    private boolean is_label_asc = true;
    private boolean is_cp_asc = true;
            
    @Inject
    private ResultService resultService;

     @PostConstruct
    public void init() {
        candidat="MACRON Emmanuel";
        resultat_type = "Resultat_psd_1" ;
        resultList = resultService.getResultByName("MACRON", "Emmanuel",resultat_type) ;
        fullNameList=resultService.getListCandidat(resultat_type);
        ResultByArrondissement=resultService.getAllResultByArrondissement("MACRON","Emmanuel",resultat_type);
        String val;
        for(Object[] o:ResultByArrondissement){
            val=(String)o[3];
            scoreGlobal+=Integer.parseInt(val);
        }
          
    }

  
        public void localeChanged(ValueChangeEvent e) { 
            scoreGlobal = 0;
             resultat_type = e.getNewValue().toString(); 
             String nom ;
             String prenom;
                fullNameList=resultService.getListCandidat(resultat_type);
                //resultat_type = "Resultat_psd_1" ;
                //TODO:Test if nomPrenom null 

                Object[] nomObj = (Object[])fullNameList.get(1);
                 nom = (String) nomObj[0] ;
                Object[] prenomObj = (Object[])fullNameList.get(1);
                 prenom = (String) prenomObj[1] ;
                

                resultList = resultService.getResultByName(nom, prenom,resultat_type) ;
                ResultByArrondissement=resultService.getAllResultByArrondissement(nom,prenom,resultat_type);
                String val;
                for(Object[] o:ResultByArrondissement){
                    val=(String)o[3];
                    scoreGlobal+=Integer.parseInt(val);
                }
            } 
    
    public List<String[]> getResultByArrondissement() {
        return ResultByArrondissement;
    }

    public void setResultByArrondissement(List<String[]> ResultByArrondissement) {
        this.ResultByArrondissement = ResultByArrondissement;
    }

   
    public String getCandidat() {
        return candidat;
    }

    public void setCandidat(String candidat) {
        LOG.info("set candidat to: {}", candidat);
      scoreGlobal=0;
        this.candidat = candidat;
        
        nomPrenom = candidat.split("_");
        //filterList.addFilter("Code_p", candidat);

            resultList = resultService.getResultByName(nomPrenom[0], nomPrenom[1],resultat_type);
            ResultByArrondissement=resultService.getAllResultByArrondissement(nomPrenom[0], nomPrenom[1],resultat_type);
           String val;
           for(Object[] o:ResultByArrondissement){
            val=(String)o[3];
            scoreGlobal+=Integer.parseInt(val);
        }
       
    }

    
      

    public String[] getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String[] nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

     public void setresultat_type (String resultat_type) {
         this.resultat_type = resultat_type ; 
     }
   
       public String getresultat_type () {
         return resultat_type ; 
     }
   
    public List<Resultat> getResultList() {
        return resultList;
    }

    public void setResultList(List<Resultat> ResultList) {
        this.resultList = ResultList;
    }

    public List<String[]> getFullNameList() {
        return fullNameList;
    }

    public void setFullNameList(List<String[]> fullNameList) {
        this.fullNameList = fullNameList;
    }

    public Integer getScoreGlobal() {
        return scoreGlobal;
    }

    public void setScoreGlobal(Integer scoreGlobal) {
        this.scoreGlobal = scoreGlobal;
    }

    public String[] getS() {
        return s;
    }

    public void setS(String[] s) {
        this.s = s;
    }
    
    public int getListResultSize() {
        return resultList.size();
    }
    
    public int getListfullNameSize() {
        return fullNameList.size();
    }
}