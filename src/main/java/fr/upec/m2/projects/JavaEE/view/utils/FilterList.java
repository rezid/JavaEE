package fr.upec.m2.projects.JavaEE.view.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterList implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> filterMap;

    public FilterList() {
        filterMap = new HashMap<>();
    }

    public void addFilter(String name, String value) {
        filterMap.put(name.toLowerCase(), value.toUpperCase());
    }

    public void deleteFilter(String name) {
        filterMap.remove(name.toLowerCase());
    }

    public List<Filter> getFilterList() {
        ArrayList<Filter> list = new ArrayList<>();
        filterMap.forEach((name, value) -> {
            Filter filter = new Filter();

            switch (name) {
                case "order":
                    filter.setName("Ordonné par");
                    break;
                case "code_postale":
                    filter.setName("Arrondissement");
                    break;
                case "mon_adresse":
                    filter.setName("Mon adresse");
                    break;
                default:
                    filter.setName("Erreur");
                    break;
            }

            switch (value) {
                case "BY_ADR_ASC":
                    filter.setValue("Adresse (ascendant)");
                    break;
                case "BY_ADR_DSC":
                    filter.setValue("Adresse (descendant)");
                    break;
                case "BY_LIB_ASC":
                    filter.setValue("Nom (ascendant)");
                    break;
                case "BY_LIB_DSC":
                    filter.setValue("Nom (descendant)");
                    break;
                case "BY_CP_ASC":
                    filter.setValue("Code postale (ascendant)");
                    break;
                case "BY_CP_DSC":
                    filter.setValue("Code postale (descendant)");
                    break;
                case "75000":
                    filter.setValue("Tous");
                    break;
                case "75001" :
                    filter.setValue("1er");
                    break;
                case "75002" :
                    filter.setValue("2éme");
                    break;
                case "75003" :
                    filter.setValue("3éme");
                    break;
                case "75004" :
                    filter.setValue("4éme");
                    break;
                case "75005" :
                    filter.setValue("5éme");
                    break;
                case "75006" :
                    filter.setValue("6éme");
                    break;
                case "75007" :
                    filter.setValue("7éme");
                    break;
                case "75008" :
                    filter.setValue("8éme");
                    break;
                case "75009" :
                    filter.setValue("9éme");
                    break;
                case "75010" :
                    filter.setValue("10éme");
                    break;
                case "75011" :
                    filter.setValue("11éme");
                    break;
                case "75012" :
                    filter.setValue("12éme");
                    break;
                case "75013" :
                    filter.setValue("13éme");
                    break;
                case "75014" :
                    filter.setValue("14éme");
                    break;
                case "75015" :
                    filter.setValue("15éme");
                    break;
                case "75016" :
                    filter.setValue("16éme");
                    break;
                case "75017" :
                    filter.setValue("17éme");
                    break;
                case "75018" :
                    filter.setValue("18éme");
                    break;
                case "75019" :
                    filter.setValue("19éme");
                    break;
                case "75020" :
                    filter.setValue("20éme");
                    break;
                case "" :
                    filter.setValue("n'as pas été défini");
                    break;
                default:
                    filter.setValue(value); // adresse
                    break;
            }

            list.add(filter);
        });
        return list;
    }
}
