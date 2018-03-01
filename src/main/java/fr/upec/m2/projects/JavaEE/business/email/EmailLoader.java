package fr.upec.m2.projects.JavaEE.business.email;

import fr.upec.m2.projects.JavaEE.utils.PropertiesUtils;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class EmailLoader {

    private Map<String, String> emails;

    @PostConstruct
    public void init() {
        emails = PropertiesUtils.loadPropertiesFromClasspath("META-INF/conf/emails");
    }

    @Produces
    @Named
    public Map<String, String> getEmails() {
        return emails;
    }
}
