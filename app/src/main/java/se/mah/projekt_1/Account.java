package se.mah.projekt_1;

//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.AuthPermission;

/**
 * Created by Gustaf on 28/04/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable, Principal {

    private String id;
    private String firstName;
    private String lastName;

    private boolean accountNonExpired = true;
    private boolean isEnabled = true;

    public String getName() {
        return firstName + " " + lastName;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return firstName + " " + lastName + ":  " + "\nid: " + id + "\nfirstname: " + firstName + "\nlastname: " + lastName + "\naccountNonExpired: " + accountNonExpired + "\nenabled: " + isEnabled;
    }
}

