package fr.upec.m2.projects.JavaEE.controller;

import fr.upec.m2.projects.JavaEE.annotation.Trace;
import org.apache.logging.log4j.*;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Random;

@Named
@SessionScoped
@Trace
public class UserNumberBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer randomInt = null;
    private Integer userNumber = null;
    private String response = null;
    private int maximum = 10;
    private int minimum = 0;

    public UserNumberBean() {
        Random random = new Random();
        randomInt = random.nextInt(maximum + 1);
    }

    public Integer getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }

    public String getResponse() {
        if ((userNumber == null) || (userNumber.compareTo(randomInt) != 0))
            return "Sorry, " + userNumber + " is incorrect.";
        else
            return "Yay! You got it!";
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }
}
