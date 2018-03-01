package fr.upec.m2.projects.JavaEE.view.auth;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.security.enterprise.credential.CallerOnlyCredential;

import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

@Named
@RequestScoped
public class SignupBacking extends AuthBacking {

	public void signup() {
		userService.registerUser(user, password);
		authenticate(withParams().credential(new CallerOnlyCredential(user.getEmail())));
	}

}