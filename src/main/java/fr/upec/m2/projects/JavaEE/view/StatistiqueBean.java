/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.upec.m2.projects.JavaEE.view;
import fr.upec.m2.projects.JavaEE.business.service.ResultService;
import fr.upec.m2.projects.JavaEE.business.service.ResultatGlobalService;
import fr.upec.m2.projects.JavaEE.business.service.StatistiqueService;
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
public class StatistiqueBean  implements Serializable{
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = LogManager.getLogger(ResultatGlobalBean.class);
    
    private List<Resultat_psd_1> resultList;
    private FilterList filterList;
    private List <String[]> resultStatistique ;
    private String[] nomPrenom;
    private boolean is_adr_asc = true;
    private boolean is_label_asc = true;
    private boolean is_cp_asc = true;
    private String statistique;
     private List <String[]> fullNameList ;
    public StatistiqueBean() {
     filterList = new FilterList();
   
    }
    
    @Inject
    private StatistiqueService statistiqueService;
    
   @Inject
    private ResultService resultService;
     @PostConstruct
    public void init() {
        resultStatistique= statistiqueService.getStatistiqueByArrondissement();
        statistique="arr";
       
    }

    public String getStatistique() {
        return statistique;
    }

    public void setStatistique(String statistique) {
        this.statistique = statistique;
        if(statistique.equalsIgnoreCase("arr")){
           resultStatistique=statistiqueService.getStatistiqueByArrondissement();
        }
        else if(statistique.equalsIgnoreCase("bur")){
            
            resultStatistique=statistiqueService.getStatistiqueByBureaux();
           }
        else{
            fullNameList=resultService.getListCandidat();
        }
        
        
    }

    public List<String[]> getFullNameList() {
        return fullNameList;
    }

    public void setFullNameList(List<String[]> fullNameList) {
        this.fullNameList = fullNameList;
    }

    
    
    public List<String[]> getResultStatistique() {
        return resultStatistique;
    }

    public void setResultStatistique(List<String[]> resultStatistique) {
        this.resultStatistique = resultStatistique;
    }
    
    public int getresultStatistiqueSize() {
        return resultStatistique.size();
    }

    public List<Filter> getFilterList() {
        return filterList.getFilterList();
    }

    
    
 
    
    

}
