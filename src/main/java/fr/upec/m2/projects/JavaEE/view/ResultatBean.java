/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.upec.m2.projects.JavaEE.view;


import fr.upec.m2.projects.JavaEE.business.ResultService;
import fr.upec.m2.projects.JavaEE.model.Resultat_psd_1;
import fr.upec.m2.projects.JavaEE.view.utils.FilterList;
import java.io.Serializable;
import java.util.Comparator;
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
    private boolean is_adr_asc = true;
    private boolean is_label_asc = true;
    private boolean is_cp_asc = true;
    private String candidat;
    private FilterList filterList;
    private List <String[]> fullNameList ;
    private String[] nomPrenom;
    private List <String []> ResultByArrondissement ;
    private Integer scoreGlobal=0;
    private String[] s = new String[4];

    
    @Inject
    private ResultService resultService;

     @PostConstruct
    public void init() {
        candidat="SARKOZY Nicolas";
        resultList= resultService.getResultByName("SARKOZY", "Nicolas") ;
        fullNameList=resultService.getListCandidat();
        ResultByArrondissement=resultService.getAllResultByArrondissement("SARKOZY","Nicolas");
        String val;
        for(Object[] o:ResultByArrondissement){
            val=(String)o[3];
            scoreGlobal+=Integer.parseInt(val);
        }
          
    }
    
    public FilterList getFilterList() {
        return filterList;
    }

    public void setFilterList(FilterList filterList) {
        this.filterList = filterList;
    }

    public ResultatBean() {
        filterList = new FilterList();
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
        
        nomPrenom = candidat.split(" ");
        //filterList.addFilter("Code_p", candidat);

            resultList = resultService.getResultByName(nomPrenom[0], nomPrenom[1]);
            ResultByArrondissement=resultService.getAllResultByArrondissement(nomPrenom[0], nomPrenom[1]);
           String val;
           for(Object[] o:ResultByArrondissement){
            val=(String)o[3];
            scoreGlobal+=Integer.parseInt(val);
        }
       
    }

    public void sortByOrder(String order) {
        LOG.info("sorting list by: {}", order);
        if (resultList.isEmpty())
            return;

        switch (order) {
            case "BY_NOM":
                if (is_adr_asc) {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getNom_du_candidat));
                    filterList.addFilter("Order", "BY_NOM");
                }
                else {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getNom_du_candidat).reversed());
                    filterList.addFilter("Order", "BY_NOM");
                }
                is_adr_asc = !is_adr_asc;
                break;

            case "BY_ARR":
                if (is_label_asc) {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getNumero_arrendissement));
                    filterList.addFilter("Order", "BY_ARR");
                }
                else {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getNumero_arrendissement).reversed());
                    filterList.addFilter("Order", "BY_ARR");
                }
                is_label_asc = !is_label_asc;
                break;

            case "BY_PRENOM":
                if (is_cp_asc) {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getPrenom_du_candidat));
                    filterList.addFilter("Order", "BY_PRENOM");
                }
                else {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getPrenom_du_candidat).reversed());
                    filterList.addFilter("Order", "BY_PRENOM");
                }
                is_cp_asc = !is_cp_asc;
                break;
            
            case "NBR_VOTE":
                if (is_label_asc) {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getNombre_de_voix_du_candidat));
                    filterList.addFilter("Order", "NBR_VOTE");
                }
                else {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getNombre_de_voix_du_candidat).reversed());
                    filterList.addFilter("Order", "NBR_VOTE");
                }
                is_label_asc = !is_label_asc;
                break;

            default:
                break;
        }
    }
    
    
    public String[] getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String[] nomPrenom) {
        this.nomPrenom = nomPrenom;
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
