package fr.upec.m2.projects.JavaEE.view.producer;

import fr.upec.m2.projects.JavaEE.business.service.UserService;
import fr.upec.m2.projects.JavaEE.model.User;
import fr.upec.m2.projects.JavaEE.view.ActiveUser;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import static java.time.Instant.now;

@SessionScoped
public class ActiveUserProducer implements Serializable {

    private static final long serialVersionUID = 1L;

    private User activeUser;

    @Inject
    private UserService userService;

    @Produces
    @Named
    @RequestScoped
    public ActiveUser getActiveUser() {
        return new ActiveUser(activeUser);
    }



}
