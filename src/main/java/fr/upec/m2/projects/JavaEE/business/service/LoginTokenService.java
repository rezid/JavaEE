package fr.upec.m2.projects.JavaEE.business.service;

import fr.upec.m2.projects.JavaEE.business.exception.InvalidUsernameException;
import fr.upec.m2.projects.JavaEE.model.LoginToken;
import fr.upec.m2.projects.JavaEE.model.User;
import fr.upec.m2.projects.JavaEE.utils.MessageDigests;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.UUID.randomUUID;

@Stateless
public class LoginTokenService {

    private static final int DEFAULT_SALT_LENGTH = 40;
    private static final long DEFAULT_PASSWORD_RESET_EXPIRATION_TIME_IN_MINUTES = TimeUnit.HOURS.toMinutes(1);
    private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-256";

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserService userService;

    public String generate(String email, String ipAddress, String description, LoginToken.TokenType tokenType) {
        Instant expiration = now().plus(14, DAYS);
        return generate(email, ipAddress, description, tokenType, expiration);
    }

    public String generate(String email, String ipAddress, String description, LoginToken.TokenType tokenType, Instant expiration) {
        String rawToken = randomUUID().toString();
        User user = userService.findByEmail(email).orElseThrow(InvalidUsernameException::new);

        LoginToken loginToken = new LoginToken();
        loginToken.setTokenHash(MessageDigests.digest(rawToken, MESSAGE_DIGEST_ALGORITHM));
        loginToken.setExpiration(expiration);
        loginToken.setDescription(description);
        loginToken.setType(tokenType);
        loginToken.setIpAddress(ipAddress);
        loginToken.setUser(user);
        user.getLoginTokens().add(loginToken);
        return rawToken;
    }

    public void remove(String loginToken) {
        entityManager.createNamedQuery("LoginToken.remove")
                .setParameter("tokenHash", MessageDigests.digest(loginToken, MESSAGE_DIGEST_ALGORITHM))
                .executeUpdate();
    }

    public void removeExpired() {
        entityManager.createNamedQuery("LoginToken.removeExpired")
                .executeUpdate();
    }
}
