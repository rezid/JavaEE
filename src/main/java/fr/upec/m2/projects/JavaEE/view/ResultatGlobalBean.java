/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.upec.m2.projects.JavaEE.view;
import fr.upec.m2.projects.JavaEE.business.ResultatGlobalService;
import fr.upec.m2.projects.JavaEE.model.Resultat;
import fr.upec.m2.projects.JavaEE.view.utils.Filter;
import fr.upec.m2.projects.JavaEE.view.utils.FilterList;
import java.io.Serializable;
import java.util.ArrayList;
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
 * @author Mohammed KAIDI 
 */
@ViewScoped
@Named
public class ResultatGlobalBean implements Serializable {
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
     */
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(ResultatGlobalBean.class);

    private List<Resultat> resultList;
    private ArrayList<Resultat> resultWinners;
    private String candidat;
    private FilterList filterList;
    private List<String[]> fullNameList;
    private String[] nomPrenom;
    private boolean is_adr_asc = true;
    private boolean is_label_asc = true;
    private boolean is_cp_asc = true;
    private String code_postale;

    public List<Resultat> getResultList() {
        return resultList;
    }

    public void setResultList(List<Resultat> resultList) {
        this.resultList = resultList;
    }

    public ArrayList<Resultat> getResultWinners() {
        return resultWinners;
    }

    public void setResultWinners(ArrayList<Resultat> resultWinners) {
        this.resultWinners = resultWinners;
    }

    private String critereSelection = "";

    public String getCritereSelection() {
        return critereSelection;
    }

    public void setCritereSelection(String critereSelection) {
        this.critereSelection = critereSelection;
    }

    public ResultatGlobalBean() {
        filterList = new FilterList();
    }

    @Inject
    private ResultatGlobalService resultGlobalService;

    @PostConstruct
    public void init() {
        resultWinners = (ArrayList<Resultat>) resultGlobalService.getWinnerByArr("1");
        resultList = resultGlobalService.getResultGlobal("1");
    }
    
    
     public void getResultatByCritere() {
        resultList = resultGlobalService.getResultGlobal(critereSelection);
        resultWinners = (ArrayList<Resultat>) resultGlobalService.getWinnerByArr(critereSelection);
    }

    public String getCandidat() {
        return candidat;
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
        if (resultList.isEmpty()) {
            return;
        }

        switch (order) {
            case "NOM_COND":
                if (is_adr_asc) {
                    resultList.sort(Comparator.comparing(Resultat::getNom_complet));
                    filterList.addFilter("Order", "NOM_COND");
                } else {
                    resultList.sort(Comparator.comparing(Resultat::getNom_complet).reversed());
                    filterList.addFilter("Order", "NOM_COND");
                }
                is_adr_asc = !is_adr_asc;
                break;

            case "BY_ARR":
                if (is_label_asc) {
                    resultList.sort(Comparator.comparing(Resultat::getNumero_arrendissement));
                    filterList.addFilter("Order", "BY_ARR");
                } else {
                    resultList.sort(Comparator.comparing(Resultat::getNumero_arrendissement).reversed());
                    filterList.addFilter("Order", "BY_ARR");
                }
                is_label_asc = !is_label_asc;
                break;

            case "BY_BRU":
                if (is_cp_asc) {
                    resultList.sort(Comparator.comparing(Resultat::getNumero_de_bureau_de_vote));
                    filterList.addFilter("Order", "BY_BRU");
                } else {
                    resultList.sort(Comparator.comparing(Resultat::getNumero_de_bureau_de_vote).reversed());
                    filterList.addFilter("Order", "BY_BRU");
                }
                is_cp_asc = !is_cp_asc;
                break;

            case "NBR_VOTE":
                if (is_label_asc) {
                    resultList.sort(Comparator.comparing(Resultat::getNombre_de_voix_du_candidat));
                    filterList.addFilter("Order", "NBR_VOTE");
                } else {
                    resultList.sort(Comparator.comparing(Resultat::getNombre_de_voix_du_candidat).reversed());
                    filterList.addFilter("Order", "NBR_VOTE");
                }
                is_label_asc = !is_label_asc;
                break;

            default:
                break;
        }
    }


}
