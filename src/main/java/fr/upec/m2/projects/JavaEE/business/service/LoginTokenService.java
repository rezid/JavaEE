package fr.upec.m2.projects.JavaEE.business.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class LoginTokenService {

    @Inject
    private UserService userService;
}
