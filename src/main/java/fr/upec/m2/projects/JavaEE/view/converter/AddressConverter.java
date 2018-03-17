package fr.upec.m2.projects.JavaEE.view.converter;

import fr.upec.m2.projects.JavaEE.business.service.AddressService;
import fr.upec.m2.projects.JavaEE.business.service.LoadDataService;
import fr.upec.m2.projects.JavaEE.model.Address;
import fr.upec.m2.projects.JavaEE.view.AddressBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter("addressConverter")
public class AddressConverter implements Converter {

    private static final Logger LOG  = LogManager.getLogger(LoadDataService.class);

    //------------------------------------------------------------------------------------------------------------------

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {


        Address address = new Address();
        if(uic.getId().equals("street_name") && value != null && value.trim().length() > 0) {
            String typed_street_name = value.trim();
            String typed_street_number = ""; //addressBean.getType_address().getStreet_number();
            String typed_zip_code = ""; //addressBean.getType_address().getZip_code();
            address.setStreet_name(typed_street_name);
            address.setStreet_number(typed_street_number);
            address.setZip_code(typed_zip_code);
        }

        final UIComponent[] uic_street_number = new UIComponent[1];
        uic.getParent().getChildren().forEach(uiComponent -> {
            if (uiComponent.getId().equals("street_number"))
                uic_street_number[0] = uiComponent;
        });

        LOG.error("street_number: {}",  uic_street_number[0].);

        /*
        AddressBean addressBean = (AddressBean) fc.getViewRoot().getViewMap().get("addressBean");
        LOG.error("addressBean: {}", addressBean);
        LOG.error("typed_address: {}", addressBean.getType_address());
        */

        LOG.info("getAsObject called in UIComponent id -{}- with string value -{}-", uic.getId(), value.trim());
        LOG.info("getAsObject returned typed_street_name -{}- and street_number -{}- and zip_code -{}-",
                address.getStreet_name(), address.getStreet_number(), address.getZip_code());

        return address;
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        String result = "";

        if(object != null && uic.getId().equals("street_name")) {
            result = String.valueOf(((Address) object).getStreet_name());
        }

        else if(object != null && uic.getId().equals("street_number")) {
            result = String.valueOf(((Address) object).getStreet_number());
        }

        LOG.info("getAsString called in UIComponent id -{}-", uic.getId());
        LOG.info("getAsString returned string -{}-", result);

        return result;
    }
}