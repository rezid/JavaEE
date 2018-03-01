package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static javax.persistence.FetchType.EAGER;

@Entity
@NamedQueries({
        @NamedQuery(
                name="User.getByEmail",
                query="SELECT _user FROM User _user WHERE _user.email = :email"),
        @NamedQuery(
                name="User.getByLoginToken",
                query="SELECT _user FROM User _user JOIN _user.loginTokens _loginToken JOIN FETCH _user.loginTokens " +
                        "WHERE _loginToken.tokenHash = :tokenHash AND _loginToken.type = :tokenType " +
                        "AND _loginToken.expiration > CURRENT_TIMESTAMP")
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 254, unique = true, nullable = false)
    private @NotNull @Email String email;

    @Column(length = 32, nullable = false)
    private @NotNull @Size(max = 32) String firstName;

    @Column(length = 32, nullable = false)
    private @NotNull @Size(max = 32) String lastName;

    @Column(nullable = false)
    private boolean emailVerified = true; // TODO: implement email verification

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Credentials credentials;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoginToken> loginTokens = new ArrayList<>();

    @Enumerated
    @ElementCollection(fetch = EAGER)
    private List<Group> groups = new ArrayList<>();

    @Column
    private Instant lastLogin;

    @Transient
    private String fullName;

    @PostLoad
    private void onLoad() {
        fullName = firstName + " " + lastName;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public List<LoginToken> getLoginTokens() {
        return loginTokens;
    }

    public void setLoginTokens(List<LoginToken> loginTokens) {
        this.loginTokens = loginTokens;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getFullName() {
        return fullName;
    }

    @Transient
    public Set<Role> getRoles() {
        return groups.stream().flatMap(g -> g.getRoles().stream()).collect(toSet());
    }

    @Transient
    public Set<String> getRolesAsStrings() {
        return getRoles().stream().map(Role::name).collect(toSet());
    }

}
