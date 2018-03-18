/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.upec.m2.projects.JavaEE.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author ramzi
 */
@Entity
@NamedQueries({
        @NamedQuery(
                name = "AdminUser.getAdmin",
                query = "SELECT _admin FROM AdminUser _admin WHERE _admin.email = :email and _admin.password =:password "),
        
})
public class AdminUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 20, nullable = false)
    private String nom;
    @Column(length = 20, nullable = false)
    private String prenom;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    @Column(length = 30,nullable = false)
    private String password ;
    @Column(length = 50, nullable = false)
    private String adresse;
    @Column(length = 20, nullable = false)
    private String Rolle;

    public AdminUser() {
    }

    public AdminUser(String nom, String prenom, String email, String password,String adresse, String Rolle) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.Rolle = Rolle;
        this.password=password;
    }

    public String getNom() {
        return nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getRolle() {
        return Rolle;
    }

    public void setRole(String Rolle) {
        this.Rolle = Rolle;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdminUser)) {
            return false;
        }
        AdminUser other = (AdminUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.upec.m2.projects.JavaEE.model.validator.AdminUser[ id=" + id + " ]";
    }
    
}
