package fr.upec.m2.projects.JavaEE.business.service;

import fr.upec.m2.projects.JavaEE.business.email.EmailService;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserService {

    @Resource
    private SessionContext sessionContext;

    @Inject
    private LoginTokenService loginTokenService;

    @Inject
    private EmailService emailService;
}
