package fr.upec.m2.projects.JavaEE.view;

import fr.upec.m2.projects.JavaEE.business.service.AddressService;
import fr.upec.m2.projects.JavaEE.model.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@Named
public class AddressBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(AddressBean.class);
    // must be like this (see AddressConverter)
    private Address type_address = new Address("", "", "");

    @Inject
    private AddressService addressService;

    //------------------------------------------------------------------------------------------------------------------

    public AddressBean() {
    }

    //------------------------------------------------------------------------------------------------------------------

    public List<Address> get_suggested_address_list() {
        String street_number_pattern = type_address.getStreet_number() + "%";
        String street_name_pattern = type_address.getStreet_name() + "%";
        String zip_code_pattern = type_address.getZip_code() + "%";
        return addressService.searchByAddressPattern(street_number_pattern, street_name_pattern, zip_code_pattern);
    }

    public List<Address> get_exact_address_list() {
        return addressService.searchByAddressPattern(type_address.getStreet_number(), type_address.getStreet_name(),
                type_address.getZip_code());
    }

    //------------------------------------------------------------------------------------------------------------------

    public Address getType_address() {
        return type_address;
    }

    public void setType_address(Address type_address) {
        this.type_address = type_address;
    }

    //------------------------------------------------------------------------------------------------------------------
}
