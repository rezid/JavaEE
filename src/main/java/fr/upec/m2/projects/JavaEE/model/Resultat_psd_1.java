package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity

@NamedQueries({
      @NamedQuery(
                name = "Resultat_psd_1.getAllResultByArrondissement",
                query = "SELECT _result.numero_arrendissement,_result.nom_du_candidat,_result.prenom_du_candidat,SUM(_result.nombre_de_voix_du_candidat)"
                        + "FROM Resultat_psd_1 _result where _result.nom_du_candidat = :nomC AND _result.prenom_du_candidat = :prenomC GROUP BY _result.numero_arrendissement,_result.nom_du_candidat,_result.prenom_du_candidat"),
     @NamedQuery(
                name = "Resultat_psd_1.getListCandidat",
                query = "SELECT DISTINCT _result.nom_du_candidat, _result.prenom_du_candidat FROM Resultat_psd_1 _result"),
   
        @NamedQuery(
                name = "Resultat_psd_1.getResultByName",
                query = "SELECT _result FROM Resultat_psd_1 _result "
                        + "WHERE _result.nom_du_candidat = :nomC AND _result.prenom_du_candidat = :prenomC"),
        
     @NamedQuery(
                name="Resultat_psd_1.getResultGlobal",
                query = "SELECT _result FROM Resultat_psd_1 _result "
        ),
      @NamedQuery(
                name="Resultat_psd_1.getStatistiqueByArrondissement",
                query = "SELECT _result.numero_arrendissement,SUM(_result.nombre_de_votants)as nombre_votans,SUM(_result.nombre_inscrits)as nombres_inscrits,SUM(_result.nombre_d_exprimes) as nombre_exprimé FROM Resultat_psd_1 _result GROUP BY _result.numero_arrendissement"
        ),    
      @NamedQuery(
                name="Resultat_psd_1.getStatistiqueByBureaux",
                query = "SELECT _result.numero_de_bureau_de_vote ,SUM(_result.nombre_de_votants)as nombre_votans,SUM(_result.nombre_inscrits)as nombres_inscrits,SUM(_result.nombre_d_exprimes) as nombre_exprimé FROM Resultat_psd_1 _result GROUP BY _result.numero_de_bureau_de_vote"
        ),    
})
public class Resultat_psd_1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private @NotNull String numero_arrendissement;

    @Column(nullable = false)
    private @NotNull String numero_de_bureau_de_vote;

    @Column(nullable = false)
    private @NotNull String code_commune;

    @Column(nullable = false)
    private @NotNull String nombre_de_votants;

    @Column(nullable = false)
    private @NotNull String nombre_d_exprimes;

    @Column(nullable = false)
    private @NotNull String nom_du_candidat;

    @Column(nullable = false)
    private @NotNull String prenom_du_candidat;

    @Column(nullable = false)
    private @NotNull String nombre_de_voix_du_candidat;
    
    @Column(nullable = false)
    private @NotNull String nombre_inscrits;


    @Transient
    private String nom_complet;

    @PostLoad
    private void init() {
        nom_complet = nom_du_candidat + " " + prenom_du_candidat;
    }

    public String getNombre_inscrits() {
        return nombre_inscrits;
    }

    public void setNombre_inscrits(String nombre_inscrits) {
        this.nombre_inscrits = nombre_inscrits;
    }

     
    public Resultat_psd_1() {
    }

    public Resultat_psd_1(@NotNull String numero_arrendissement, @NotNull String numero_de_bureau_de_vote, @NotNull String code_commune, @NotNull String nombre_de_votants, @NotNull String nombre_inscrits, @NotNull String nombre_d_exprimes, @NotNull String nom_du_candidat, @NotNull String prenom_du_candidat, @NotNull String nombre_de_voix_du_candidat) {
        this.numero_arrendissement = numero_arrendissement;
        this.numero_de_bureau_de_vote = numero_de_bureau_de_vote;
        this.code_commune = code_commune;
        this.nombre_de_votants = nombre_de_votants;
        this.nombre_d_exprimes = nombre_d_exprimes;
        this.nom_du_candidat = nom_du_candidat;
        this.prenom_du_candidat = prenom_du_candidat;
        this.nombre_de_voix_du_candidat = nombre_de_voix_du_candidat;
        this.nombre_inscrits=nombre_inscrits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero_arrendissement() {
        return numero_arrendissement;
    }

    public void setNumero_arrendissement(String numero_arrendissement) {
        this.numero_arrendissement = numero_arrendissement;
    }

    public String getNumero_de_bureau_de_vote() {
        return numero_de_bureau_de_vote;
    }

    public void setNumero_de_bureau_de_vote(String numero_de_bureau_de_vote) {
        this.numero_de_bureau_de_vote = numero_de_bureau_de_vote;
    }

    public String getCode_commune() {
        return code_commune;
    }

    public void setCode_commune(String code_commune) {
        this.code_commune = code_commune;
    }

    public String getNombre_de_votants() {
        return nombre_de_votants;
    }

    public void setNombre_de_votants(String nombre_de_votants) {
        this.nombre_de_votants = nombre_de_votants;
    }

    public String getNombre_d_exprimes() {
        return nombre_d_exprimes;
    }

    public void setNombre_d_exprimes(String nombre_d_exprimes) {
        this.nombre_d_exprimes = nombre_d_exprimes;
    }

    public String getNom_du_candidat() {
        return nom_du_candidat;
    }

    public void setNom_du_candidat(String nom_du_candidat) {
        this.nom_du_candidat = nom_du_candidat;
    }

    public String getPrenom_du_candidat() {
        return prenom_du_candidat;
    }

    public void setPrenom_du_candidat(String prenom_du_candidat) {
        this.prenom_du_candidat = prenom_du_candidat;
    }

    public String getNombre_de_voix_du_candidat() {
        return nombre_de_voix_du_candidat;
    }

    public void setNombre_de_voix_du_candidat(String nombre_de_voix_du_candidat) {
        this.nombre_de_voix_du_candidat = nombre_de_voix_du_candidat;
    }

    public String getNom_complet() {
        return nom_complet;
    }

    public void setNom_complet(String nom_complet) {
        this.nom_complet = nom_complet;
    }
}
