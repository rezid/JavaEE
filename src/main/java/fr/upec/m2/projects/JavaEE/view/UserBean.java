package fr.upec.m2.projects.JavaEE.view;

import fr.upec.m2.projects.JavaEE.annotation.Trace;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Named
@RequestScoped
@Trace
public class UserBean {
    private String firstName;
    private String lastName;
    @Email(message = "Email: n'ai pas valide.")
    @NotEmpty(message = "Email: obligatoire")
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
