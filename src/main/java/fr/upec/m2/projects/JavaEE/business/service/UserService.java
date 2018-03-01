package fr.upec.m2.projects.JavaEE.business.service;

import fr.upec.m2.projects.JavaEE.business.email.EmailService;
import fr.upec.m2.projects.JavaEE.business.email.EmailTemplate;
import fr.upec.m2.projects.JavaEE.business.email.EmailUser;
import fr.upec.m2.projects.JavaEE.business.exception.DuplicateEntityException;
import fr.upec.m2.projects.JavaEE.business.exception.InvalidPasswordException;
import fr.upec.m2.projects.JavaEE.business.exception.InvalidUsernameException;
import fr.upec.m2.projects.JavaEE.model.Credentials;
import fr.upec.m2.projects.JavaEE.model.Group;
import fr.upec.m2.projects.JavaEE.model.LoginToken;
import fr.upec.m2.projects.JavaEE.model.User;
import fr.upec.m2.projects.JavaEE.utils.MessageDigests;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Stateless
public class UserService implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_SALT_LENGTH = 40;
    private static final long DEFAULT_PASSWORD_RESET_EXPIRATION_TIME_IN_MINUTES = TimeUnit.HOURS.toMinutes(1);
    private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-256";

    @Resource
    private SessionContext sessionContext;

    @Inject
    private LoginTokenService loginTokenService;

    @Inject
    private EmailService emailService;

    @PersistenceContext
    private EntityManager entityManager;


    public void registerUser(User user, String password) {
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEntityException();
        }

        setCredentials(user, password);

        if (!user.getGroups().contains(Group.USER)) {
            user.getGroups().add(Group.USER);
        }

        save(user);
    }

    public User update(User user) {
        User existingUser = manage(user);

        if (!user.getEmail().equals(existingUser.getEmail())) { // Email changed.
            Optional<User> otherUser = findByEmail(user.getEmail());

            if (otherUser.isPresent()) {
                if (!user.equals(otherUser.get())) {
                    throw new DuplicateEntityException();
                }
                else {
                    // Since email verification status can be updated asynchronous, the DB status is leading.
                    // Set the current user to whatever is already in the DB.
                    user.setEmailVerified(otherUser.get().isEmailVerified());
                }
            }
            else {
                user.setEmailVerified(false);
            }
        }

        return update(user);
    }

    public void updatePassword(User user, String password) {
        User existingUser = manage(user);
        setCredentials(existingUser, password);
        update(existingUser);
    }

    public void updatePassword(String loginToken, String password) {
        Optional<User> user = findByLoginToken(loginToken, LoginToken.TokenType.RESET_PASSWORD);

        if (user.isPresent()) {
            updatePassword(user.get(), password);
            loginTokenService.remove(loginToken);
        }
    }

    public void requestResetPassword(String email, String ipAddress, String callbackUrlFormat) {
        User user = findByEmail(email).orElseThrow(InvalidUsernameException::new);
        ZonedDateTime expiration = ZonedDateTime.now().plusMinutes(DEFAULT_PASSWORD_RESET_EXPIRATION_TIME_IN_MINUTES);
        String token = loginTokenService.generate(email, ipAddress, "Reset Password", LoginToken.TokenType.RESET_PASSWORD, expiration.toInstant());

        EmailTemplate emailTemplate = new EmailTemplate("resetPassword")
                .setToUser(new EmailUser(user))
                .setCallToActionURL(String.format(callbackUrlFormat, token));

        Map<String, Object> messageParameters = new HashMap<>();
        messageParameters.put("expiration", expiration);
        messageParameters.put("ip", ipAddress);

        emailService.sendTemplate(emailTemplate, messageParameters);
    }

    public Optional<User> findByEmail(String email) {
        return getOptionalSingleResult(entityManager.createNamedQuery("User.getByEmail")
                .setParameter("email", email));
    }

    public Optional<User> findByLoginToken(String loginToken, LoginToken.TokenType type) {
        return getOptionalSingleResult(entityManager.createNamedQuery("User.getByLoginToken")
                .setParameter("tokenHash", MessageDigests.digest(loginToken, MESSAGE_DIGEST_ALGORITHM))
                .setParameter("tokenType", type));
    }

    public User getByEmailAndPassword(String email, String password) {
        User user = findByEmail(email).orElseThrow(InvalidUsernameException::new);
        Credentials credentials = user.getCredentials();

        if (credentials == null) {
            throw new InvalidUsernameException();
        }

        byte[] passwordHash = MessageDigests.digest(password, credentials.getSalt(), MESSAGE_DIGEST_ALGORITHM);

        if (!Arrays.equals(passwordHash, credentials.getPasswordHash())) {
            throw new InvalidPasswordException();
        }

        return user;
    }

    public User getActiveUser() {
        return findByEmail(sessionContext.getCallerPrincipal().getName()).orElse(null);
    }

    private static void setCredentials(User user, String password) {
        byte[] salt = generateSalt(DEFAULT_SALT_LENGTH);
        byte[] passwordHash = MessageDigests.digest(password, salt, MESSAGE_DIGEST_ALGORITHM);

        Credentials credentials = user.getCredentials();

        if (credentials == null) {
            credentials = new Credentials();
            credentials.setUser(user);
            user.setCredentials(credentials);
        }

        credentials.setPasswordHash(passwordHash);
        credentials.setSalt(salt);
    }

    private static byte[] generateSalt(int saltLength) {
        byte[] salt = new byte[saltLength];
        ThreadLocalRandom.current().nextBytes(salt);
        return salt;
    }

    private User save(User entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        }
        else {
            return entityManager.merge(entity);
        }
    }

    private User manage(User entity) {
        if (entity.getId() == null) {
            throw new PersistenceException("User Entity has no ID.");
        }

        if (entityManager.contains(entity)) {
            return entity;
        }

        User managed = entityManager.find(User.class, entity.getId());

        if (managed == null) {
            throw new PersistenceException("User Entity has in meanwhile been deleted.");
        }

        return managed;
    }

    private Optional<User> getOptionalSingleResult(Query query) {
        User user = null;

        try {
            user = (User) query.getSingleResult();
        }
        catch (NoResultException e) {
            user = null;
        }

        return Optional.ofNullable(user);
    }

}
