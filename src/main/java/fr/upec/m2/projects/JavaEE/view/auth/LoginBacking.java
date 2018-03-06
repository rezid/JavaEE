package fr.upec.m2.projects.JavaEE.view.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.credential.UsernamePasswordCredential;

import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

@Named
@RequestScoped
public class LoginBacking extends AuthBacking {

	private boolean loginToContinue = true;

	public void login() {
		authenticate(withParams()
			.credential(new UsernamePasswordCredential(user.getEmail(), password))
			.newAuthentication(!loginToContinue)
			.rememberMe(rememberMe));
	}

}