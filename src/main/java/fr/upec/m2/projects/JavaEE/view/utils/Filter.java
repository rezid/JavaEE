package fr.upec.m2.projects.JavaEE.view.utils;

import java.io.Serializable;

public class Filter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String value;

    public Filter() {
    }

    public Filter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
