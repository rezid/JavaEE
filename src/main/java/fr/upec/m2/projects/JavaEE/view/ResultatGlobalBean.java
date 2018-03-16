/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.upec.m2.projects.JavaEE.view;
import fr.upec.m2.projects.JavaEE.business.service.ResultatGlobalService;
import fr.upec.m2.projects.JavaEE.model.Resultat_psd_1;
import fr.upec.m2.projects.JavaEE.view.utils.Filter;
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

/**
 *
 * @author d3vos
 */
@ViewScoped
@Named
public class ResultatGlobalBean  implements Serializable{
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = LogManager.getLogger(ResultatGlobalBean.class);
    
    private List<Resultat_psd_1> resultList;
    private String candidat;
    private FilterList filterList;
    private List <String[]> fullNameList ;
    private String[] nomPrenom;
    private boolean is_adr_asc = true;
    private boolean is_label_asc = true;
    private boolean is_cp_asc = true;
    private String code_postale;


    public ResultatGlobalBean() {
     filterList = new FilterList();
    }
    
    @Inject
    private ResultatGlobalService resultGlobalService;

     @PostConstruct
    public void init() {
        resultList= resultGlobalService.getResultGlobal();
       // fullNameList=resultService.getListCandidat();   
    }

    public String getCandidat() {
        return candidat;
    }

    public void setCandidat(String candidat) {
        LOG.info("set candidat to: {}", candidat);
      
        this.candidat = candidat;
        
        nomPrenom = candidat.split(" ");
        //filterList.addFilter("Code_p", candidat);
       //resultList = resultService.getResultByName(nomPrenom[0], nomPrenom[1]);  
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
    

    public List<Filter> getFilterList() {
        return filterList.getFilterList();
    }

    public int getListSize() {
        return resultList.size();
    }
    
    public String getCodePostale() {
        return code_postale;
    }

    public void sortByOrder(String order) {
        LOG.info("sorting list by: {}", order);
        if (resultList.isEmpty())
            return;

        switch (order) {
            case "NOM_COND":
                if (is_adr_asc) {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getNom_complet));
                    filterList.addFilter("Order", "NOM_COND");
                }
                else {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getNom_complet).reversed());
                    filterList.addFilter("Order", "NOM_COND");
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

            case "BY_BRU":
                if (is_cp_asc) {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getNumero_de_bureau_de_vote));
                    filterList.addFilter("Order", "BY_BRU");
                }
                else {
                    resultList.sort(Comparator.comparing(Resultat_psd_1::getNumero_de_bureau_de_vote).reversed());
                    filterList.addFilter("Order", "BY_BRU");
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
    
    

}
