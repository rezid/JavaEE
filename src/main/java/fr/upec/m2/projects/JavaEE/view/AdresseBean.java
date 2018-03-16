package fr.upec.m2.projects.JavaEE.view;

import fr.upec.m2.projects.JavaEE.business.service.AdresseService;
import fr.upec.m2.projects.JavaEE.model.Adresse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Named
public class AdresseBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(AdresseBean.class);

    private List<Adresse> adresseList;

    @Inject
    private AdresseService adresseService;

    public AdresseBean() {

    }

    @PostConstruct
    public void init() {
        adresseList = adresseService.getAllAdresse();
    }

    public List<Adresse> getAdresseList() {
        return adresseList;
    }

    public void setAdresseList(List<Adresse> adresseList) {
        this.adresseList = adresseList;
    }

    public List<String> complete(String word) {
        List<String> list = new ArrayList<>();

        int i = 0;
        for (Adresse adresse : adresseList) {
            String temp = adresse.getAdresse() + " -- " + adresse.getCode_postale();
            if (temp.toLowerCase().contains(word.toLowerCase())) {
                list.add(temp);
                i++;
            }

            if (i == 7)
                break;
        }
        return list;
    }


}
