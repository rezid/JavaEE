package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity

@NamedQueries({
      @NamedQuery(
                name = "Resultat_psd_2.getAllResultByArrondissement",
                query = "SELECT _result.numero_arrendissement,_result.nom_du_candidat,_result.prenom_du_candidat,SUM(_result.nombre_de_voix_du_candidat)"
                        + "FROM Resultat_psd_2 _result where _result.nom_du_candidat = :nomC AND _result.prenom_du_candidat = :prenomC GROUP BY _result.numero_arrendissement,_result.nom_du_candidat,_result.prenom_du_candidat"),
     @NamedQuery(
                name = "Resultat_psd_2.getListCandidat",
                query = "SELECT _result.nom_du_candidat,_result.prenom_du_candidat,SUM(_result.nombre_de_voix_du_candidat),SUM(_result.nombre_de_votants) FROM Resultat_psd_2 _result GROUP BY _result.nom_du_candidat,_result.prenom_du_candidat " ),
     @NamedQuery(
                name="Resultat_psd_2.getResultGlobal",
                query = "SELECT _result FROM Resultat_psd_2 _result "
        ),
        @NamedQuery(
                name = "Resultat_psd_2.getResultByName",
                query = "SELECT _result FROM Resultat_psd_2 _result "
                        + "WHERE _result.nom_du_candidat = :nomC AND _result.prenom_du_candidat = :prenomC"),
        @NamedQuery(
                name="Resultat_psd_2.getStatistiqueByArrondissement",
                query = "SELECT _result.numero_arrendissement,SUM(_result.nombre_de_votants)as nombre_votans,SUM(_result.nombre_inscrits)as nombres_inscrits,SUM(_result.nombre_d_exprimes) as nombre_exprim� FROM Resultat_psd_2 _result GROUP BY _result.numero_arrendissement"
        ),    
      @NamedQuery(
                name="Resultat_psd_2.getStatistiqueByBureaux",
                query = "SELECT _result.numero_de_bureau_de_vote ,SUM(_result.nombre_de_votants)as nombre_votans,SUM(_result.nombre_inscrits)as nombres_inscrits,SUM(_result.nombre_d_exprimes) as nombre_exprim� FROM Resultat_psd_2 _result GROUP BY _result.numero_de_bureau_de_vote"
        )
})

public class Resultat_psd_2 extends Resultat implements Serializable {

    
    public Resultat_psd_2() {
    }
    
     public Resultat_psd_2(String numero_arrendissement, String numero_de_bureau_de_vote, String code_commune, String nombre_de_votants, String nombre_d_exprimes, String nom_du_candidat, String prenom_du_candidat, String nombre_de_voix_du_candidat,String nombre_inscrits) {
        super(numero_arrendissement, numero_de_bureau_de_vote, code_commune, nombre_de_votants, nombre_d_exprimes, nom_du_candidat, prenom_du_candidat, nombre_de_voix_du_candidat, nombre_inscrits);
     }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

  
}
