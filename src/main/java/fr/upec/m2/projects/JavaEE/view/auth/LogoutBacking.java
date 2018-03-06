package fr.upec.m2.projects.JavaEE.view.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;


@Named
@RequestScoped
public class LogoutBacking {

	private static final Logger LOG = LogManager.getLogger(AuthBacking.class);

	private static void redirect(String url) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			LOG.error("redirect failed: {}", e.getMessage());
		}
	}

	public void logout() throws ServletException {
		((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).logout();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		LOG.error("auth.message.warn.logged_out");
		redirect("");
	}

}