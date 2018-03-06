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
                default:
                    filter.setValue("Erreur");
                    break;
            }

            list.add(filter);
        });
        return list;
    }
}
