package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@NamedQueries({
    @NamedQuery(
            name="LoginToken.remove",
            query="DELETE FROM LoginToken _loginToken WHERE _loginToken.tokenHash = :tokenHash"),
    @NamedQuery(
            name="LoginToken.removeExpired",
            query="DELETE FROM LoginToken _loginToken WHERE _loginToken.expiration < CURRENT_TIMESTAMP")
})
public class LoginToken implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum TokenType {
        REMEMBER_ME,
        API,
        RESET_PASSWORD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 32, unique = true)
    private byte[] tokenHash;

    @Column
    private Instant created;

    @Column
    private Instant expiration;

    @Column(length = 45)
    private String ipAddress;

    @Column
    private String description;

    @ManyToOne(optional = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(byte[] tokenHash) {
        this.tokenHash = tokenHash;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    @PrePersist
    public void setTimestamps() {
        created = Instant.now();

        if (expiration == null)
            expiration = created.plus(1, ChronoUnit.MONTHS);
    }
}
