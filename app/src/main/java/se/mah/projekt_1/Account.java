package se.mah.projekt_1;

//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.security.auth.AuthPermission;

/**
 * Created by Gustaf on 28/04/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {

    private String id;
    private String firstName;
    private String lastName;

    private boolean accountNonExpired = true;
    private boolean isEnabled = true;

    private RfidKey rfidKey;

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
        return firstName + " " + lastName + ":  " + "\nid: " + id + "\nfirstname: " + firstName + "\nlastname: " + lastName + "\nrfid: " + rfidKey.getId() + "\naccountNonExpired: " + accountNonExpired + "\nenabled: " + isEnabled;
    }

    public RfidKey getRfidKey() {
        return rfidKey;
    }

    public void setRfidKey(RfidKey rfidKey) {
        this.rfidKey = rfidKey;
    }

    public void createAccountFromMap(LinkedHashMap<String, Object> accountMap) {
        firstName = (String) accountMap.get("firstName");
        lastName = (String) accountMap.get("lastName");
        id = (String) accountMap.get("id");
        accountNonExpired = (Boolean) accountMap.get("accountNonExpired");
        isEnabled = (Boolean) accountMap.get("enabled");
        rfidKey = new RfidKey((String) ((LinkedHashMap<String, Object>) accountMap.get("rfidKey")).get("id"));
    }
}

