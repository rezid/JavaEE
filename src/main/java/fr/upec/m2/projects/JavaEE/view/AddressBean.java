package fr.upec.m2.projects.JavaEE.view;

import fr.upec.m2.projects.JavaEE.business.AddressService;
import fr.upec.m2.projects.JavaEE.model.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@Named
public class AddressBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(AddressBean.class);
    private String typed_street_number;
    private String typed_street_name;
    private String typed_zip_code;

    @Inject
    private AddressService addressService;

    //------------------------------------------------------------------------------------------------------------------

    public AddressBean() {
    }

    //------------------------------------------------------------------------------------------------------------------

    public List<String> get_suggested_address_list(int choice) {
        String street_number_pattern = emptyStringIfNull(typed_street_number) + "%";
        String street_name_pattern = emptyStringIfNull(typed_street_name) + "%";
        String zip_code_pattern = emptyStringIfNull(typed_zip_code) + "%";
        List<Address> address_list =
                addressService.searchByAddressPattern(street_number_pattern, street_name_pattern, zip_code_pattern);

        List<String> result_list = new ArrayList<>();

        // add street names
        if (choice == 1) {
            address_list.forEach(address -> result_list.add(address.getStreet_name()));
        }

        // add street numbers
        else if (choice == 2) {
            address_list.forEach(address -> result_list.add(address.getStreet_number()));
        }

        // add zip codes
        else if (choice == 3) {
            address_list.forEach(address -> result_list.add(address.getZip_code()));
        }

        return result_list;
    }

    public List<Address> get_exact_address_list() {
        return addressService.searchByAddressPattern(
                emptyStringIfNull(typed_street_number),
                emptyStringIfNull(typed_street_name),
                emptyStringIfNull(typed_zip_code)
        );
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getTyped_street_number() {
        return typed_street_number;
    }

    public void setTyped_street_number(String typed_street_number) {
        this.typed_street_number = typed_street_number;
    }

    public String getTyped_street_name() {
        return typed_street_name;
    }

    public void setTyped_street_name(String typed_street_name) {
        this.typed_street_name = typed_street_name;
    }

    public String getTyped_zip_code() {
        return typed_zip_code;
    }

    public void setTyped_zip_code(String typed_zip_code) {
        this.typed_zip_code = typed_zip_code;
    }

    //------------------------------------------------------------------------------------------------------------------

    private String emptyStringIfNull(String object) {
        return object == null ? "" : object.trim();
    }

    //------------------------------------------------------------------------------------------------------------------
}
