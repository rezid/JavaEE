package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@NamedQueries({
    
    
     @NamedQuery(
                name = "Bureau.getAllAdresse",
                query = "SELECT DISTINCT _bureau.Adresse_bureau  FROM Bureau _bureau"),
    
     @NamedQuery(
                name = "Bureau.updateData",
                query = "UPDATE Bureau b SET b.numero_bureau = :numBureau , b.Adresse_bureau = :adresse , b.code_postal_bureau = :codePostal , b.label_bureau = :labelBureau WHERE b.id =:id "),
        @NamedQuery(
                name = "Bureau.getAllBureau",
                query = "SELECT _bureau FROM Bureau _bureau"),
        @NamedQuery(
                name = "Bureau.getBurreauByArrondissement",
                query = "SELECT _bureau FROM Bureau _bureau WHERE _bureau.code_postal_bureau = :code_postale"),
})
public class Bureau implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 10, unique = true, nullable = false)
    private @NotNull String objectID; // id dans le site de paris

    @Column(length = 10, nullable = false)
    private @NotNull String numero_bureau;

    @Column(nullable = false)
    private @NotNull String label_bureau;

    @Column(nullable = false)
    private @NotNull String Adresse_bureau;

    @Column(length = 10, nullable = false)
    private @NotNull String code_postal_bureau;

    // constructors

    public Bureau() {
    }

    public Bureau(@NotNull String objectID, @NotNull String numero_bureau, @NotNull String label_bureau, @NotNull String adresse_bureau, @NotNull String code_postal_bureau) {
        this.objectID = objectID;
        this.numero_bureau = numero_bureau;
        this.label_bureau = label_bureau;
        Adresse_bureau = adresse_bureau;
        this.code_postal_bureau = code_postal_bureau;
    }
// getters ans setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getNumero_bureau() {
        return numero_bureau;
    }

    public void setNumero_bureau(String numero_bureau) {
        this.numero_bureau = numero_bureau;
    }

    public String getLabel_bureau() {
        return label_bureau;
    }

    public void setLabel_bureau(String label_bureau) {
        this.label_bureau = label_bureau;
    }

    public String getAdresse_bureau() {
        return Adresse_bureau;
    }

    public void setAdresse_bureau(String adresse_bureau) {
        Adresse_bureau = adresse_bureau;
    }

    public String getCode_postal_bureau() {
        return code_postal_bureau;
    }

    public void setCode_postal_bureau(String code_postal_bureau) {
        this.code_postal_bureau = code_postal_bureau;
    }
}
