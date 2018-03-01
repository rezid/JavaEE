package fr.upec.m2.projects.JavaEE.view.auth;

import fr.upec.m2.projects.JavaEE.business.service.UserService;
import fr.upec.m2.projects.JavaEE.model.Group;
import fr.upec.m2.projects.JavaEE.model.User;
import fr.upec.m2.projects.JavaEE.model.validator.Password;
import fr.upec.m2.projects.JavaEE.view.ActiveUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import java.io.IOException;

import static javax.security.enterprise.AuthenticationStatus.SEND_CONTINUE;
import static javax.security.enterprise.AuthenticationStatus.SEND_FAILURE;


public abstract class AuthBacking {

	private static final Logger LOG = LogManager.getLogger(AuthBacking.class);
	protected User user;
	protected @NotNull @Password String password;
	protected boolean rememberMe;

	@Inject
	protected UserService userService;

	@Inject
	private SecurityContext securityContext;

	@Inject
	private ActiveUser activeUser;

	@PostConstruct
    public void init() {
        if (activeUser.isPresent()) {
            LOG.info("auth.message.warn.already_logged_in");
            redirect("user/profile");
        }
        else {
            user = new User();
        }
    }

	protected static void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException e) {
            LOG.error("redirect failed: {}", e.getMessage());
        }
    }

    public static void validationFailed() {
        FacesContext.getCurrentInstance().validationFailed();
    }

    public static void responseComplete() {
        FacesContext.getCurrentInstance().responseComplete();
    }

	protected void authenticate(AuthenticationParameters parameters) {
		AuthenticationStatus status = securityContext.authenticate(
                (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(),
                (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse(),
                parameters);

		if (status == SEND_FAILURE) {
            LOG.info("auth.message.error.failure");
			validationFailed();
		}
		else if (status == SEND_CONTINUE) {
			responseComplete(); // Prevent JSF from rendering a response so authentication mechanism can continue.
        }
		else if (activeUser.hasGroup(Group.ADMIN)) {
			redirect("admin/users");
		}
		else if (activeUser.hasGroup(Group.USER)) {
			redirect("user/profile");
		}
		else {
			redirect("");
		}
	}

	public User getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

}