package se.mah.projekt_1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Gustaf on 28/04/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class XSRFToken implements Serializable{

    private SessionInformation details;
    private boolean authenticated;
    private Account principal;

    public String toString() {
        String res = "";
        if(details != null)
            res += details.toString() + "\t";
        else
            res += "SessionInformation = null \t";
        res += authenticated + "\t";
        if(principal != null)
            res += principal.toString() + "\t";
        else
            res += "Account = null";
        return res;
    }

    public SessionInformation getDetails() {
        return details;
    }

    public void setDetails(SessionInformation details) {
        this.details = details;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Account getPrincipal() {
        return principal;
    }

    public void setPrincipal(Account principal) {
        this.principal = principal;
    }
}
