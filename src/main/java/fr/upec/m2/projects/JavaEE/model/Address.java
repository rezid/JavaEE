package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(indexes= {
        @Index(name = "STREET_NUMBER_INDEX", columnList = "STREET_NUMBER"),
        @Index(name = "STREET_NAME_INDEX", columnList = "STREET_NAME"),
        @Index(name = "ZIP_CODE_INDEX", columnList = "ZIP_CODE"),
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"STREET_NUMBER", "STREET_NAME", "ZIP_CODE"})
})
@NamedQueries({
        @NamedQuery(
                name = "Address.searchByAddressPattern",
                query = "SELECT address FROM Address address " +
                        "WHERE address.street_number LIKE :street_number_pattern "+
                        "AND address.street_name LIKE :street_name_pattern " +
                        "AND address.zip_code LIKE :zip_code_pattern"),
})
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private @NotNull String street_number; // not an int because can be '2A' for example

    @Column(nullable = false)
    private @NotNull String street_name; // ex: AV CHARLE DEUX

    @Column(length = 5, nullable = false)
    private @NotNull String zip_code; // ex: 75005

    @Column(nullable = false)
    private @NotNull Double latitude_gps_coordinate; // ex: 48.4598

    @Column(nullable = false)
    private @NotNull Double longitude_gps_coordinate; // ex: 2.6758

    //------------------------------------------------------------------------------------------------------------------

    public Address() {
    }

    public Address(@NotNull String street_number, @NotNull String street_name, @NotNull String zip_code) {
        this.street_number = street_number;
        this.street_name = street_name;
        this.zip_code = zip_code;
    }

    public Address(@NotNull String street_number, @NotNull String street_name, @NotNull String zip_code, @NotNull Double latitude_gps_coordinate, @NotNull Double longitude_gps_coordinate) {
        this.street_number = street_number;
        this.street_name = street_name;
        this.zip_code = zip_code;
        this.latitude_gps_coordinate = latitude_gps_coordinate;
        this.longitude_gps_coordinate = longitude_gps_coordinate;
    }

    //------------------------------------------------------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getLatitude_gps_coordinate() {
        return latitude_gps_coordinate;
    }

    public void setLatitude_gps_coordinate(Double latitude_gps_coordinate) {
        this.latitude_gps_coordinate = latitude_gps_coordinate;
    }

    public Double getLongitude_gps_coordinate() {
        return longitude_gps_coordinate;
    }

    public void setLongitude_gps_coordinate(Double longitude_gps_coordinate) {
        this.longitude_gps_coordinate = longitude_gps_coordinate;
    }

    //------------------------------------------------------------------------------------------------------------------
}
