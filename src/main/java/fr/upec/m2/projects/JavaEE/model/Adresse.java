package fr.upec.m2.projects.JavaEE.model;


import fr.upec.m2.projects.JavaEE.view.AdresseBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Adresse.getAllAdresse",
                query = "SELECT _adresse FROM Adresse _adresse"),
})
public class Adresse implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private @NotNull String numero_voie;

    @Column(nullable = false)
    private @NotNull String adresse;

    @Column(nullable = false)
    private @NotNull String code_postale;

    @JoinColumn(nullable=false)
    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    private @NotNull Point2D coordinates;

    public Adresse() {
    }

    public Adresse(@NotNull String numero_voie, @NotNull String adresse, @NotNull String code_postale, @NotNull Point2D coordinates) {
        this.numero_voie = numero_voie;
        this.adresse = adresse;
        this.code_postale = code_postale;
        this.coordinates = coordinates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero_voie() {
        return numero_voie;
    }

    public void setNumero_voie(String numero_voie) {
        this.numero_voie = numero_voie;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCode_postale() {
        int i = 0;
        try {
            i = Integer.parseInt(code_postale) + 75000;
        } catch (NumberFormatException e) {
            return ""; // Adresse pas defini dans le site de paris
        }
        return Integer.toString(i);
    }

    public void setCode_postale(String code_postale) {
        this.code_postale = code_postale;
    }

    public Point2D getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point2D coordinates) {
        this.coordinates = coordinates;
    }
}
