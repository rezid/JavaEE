package fr.upec.m2.projects.JavaEE.view;
import fr.upec.m2.projects.JavaEE.business.service.AdminService;
import fr.upec.m2.projects.JavaEE.model.AdminUser;
import fr.upec.m2.projects.JavaEE.model.Bureau;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;


@ViewScoped
@Named
public class AdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LogManager.getLogger(BureauBean.class);

    private AdminUser admin;
    private String email;
    private String password;

    @Inject
    private AdminService adminService;

   
    

    @PostConstruct
    public void init() {
      adminService.getAdmin("ramzi.chebbak@gmail.com", "123456");
     }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AdminUser getAdmin() {
        return admin;
    }

    public void setAdmin(AdminUser admin) {
        this.admin = admin;
    }

   

    public String login(){
       try{
           Boolean admin1 = adminService.getAdmin(email, password);
              
                   return "succes";}
       
       catch( Exception e){
           return "failure";
          
       }
           
    
        
    }  }    
    

    

