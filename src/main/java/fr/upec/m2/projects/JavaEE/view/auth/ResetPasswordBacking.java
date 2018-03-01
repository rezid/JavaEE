package fr.upec.m2.projects.JavaEE.view.auth;


import fr.upec.m2.projects.JavaEE.business.service.UserService;
import fr.upec.m2.projects.JavaEE.model.LoginToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;


@Named
@RequestScoped
public class ResetPasswordBacking extends AuthBacking {

	private String token = "sdf45s6df"; // TODO: Inject

	@Inject
	private UserService userService;

	private Logger LOG = LogManager.getLogger(ResetPasswordBacking.class);

	@Override
	@PostConstruct
	public void init() {
		super.init();

		if (token != null && !userService.findByLoginToken(token, LoginToken.TokenType.RESET_PASSWORD).isPresent()) {
			LOG.info("reset_password.message.warn.invalid_token");
			redirect("reset-password");
		}
	}

	public void requestResetPassword() {
		String email = user.getEmail();
		String ipAddress = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
		String requestURL = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

		try {
			userService.requestResetPassword(email, ipAddress, requestURL + "?token=%s");
		}
		catch (Exception e) {
			LOG.info("{} made a failed attempt to reset password for email {}: {}", ipAddress, email, e.getMessage());
		}

        LOG.info("reset_password.message.info.email_sent");
	}

	public void saveNewPassword() {
		userService.updatePassword(token, password);
        LOG.info("reset_password.message.info.password_changed");
		redirect("user/profile");
	}

	public String getToken() {
		return token;
	}

}