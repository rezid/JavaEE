package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(indexes= {
        @Index(name = "ZIP_CODE_INDEX", columnList = "ZIP_CODE"),
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"STREET_NUMBER", "STREET_NAME", "ZIP_CODE"})
})
@NamedQueries({
        @NamedQuery(
                name = "Office.getAllOffice",
                query = "SELECT _office FROM Office _office"),
        @NamedQuery(
                name = "Office.getAllOfficeByDistrict",
                query = "SELECT _office FROM Office _office " +
                        "WHERE _office.zip_code = :zip_code"),
        @NamedQuery(
                name = "Office.getOfficeByNumber",
                query = "SELECT _office FROM Office _office " +
                        "WHERE _office.office_number = :office_number"),
})
public class Office implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String office_number; // ex: 18-15

    @Column(nullable = false)
    private @NotNull String name; // ex: STADE ELISABETH

    @Column(nullable = false)
    private @NotNull String street_number; // not an int because can be '2A' for example

    @Column(nullable = false)
    private @NotNull String street_name; // ex: AV CHARLE DEUX

    @Column(length = 5, nullable = false)
    private @NotNull String zip_code; // ex: 75005

    //------------------------------------------------------------------------------------------------------------------

    public Office() {
    }

    public Office(String office_number, @NotNull String name,@NotNull String street_number,
                  @NotNull String street_name, @NotNull String zip_code) {
        this.office_number = office_number;
        this.name = name;
        this.street_number = street_number;
        this.street_name = street_name;
        this.zip_code = zip_code;
    }

    //------------------------------------------------------------------------------------------------------------------


    public String getOffice_number() {
        return office_number;
    }

    public void setOffice_number(String office_number) {
        this.office_number = office_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    //------------------------------------------------------------------------------------------------------------------
}
