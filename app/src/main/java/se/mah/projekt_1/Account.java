package se.mah.projekt_1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.security.auth.AuthPermission;

/**
 * Created by Gustaf on 28/04/2016.
 * Represents a user
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private String username;

    private boolean accountNonExpired = true;
    private boolean isEnabled = true;
    private String encryptedUserCredentials;

    private RfidKey rfidKey;

    /**
     * Returns the users name
     * @return firstname lastname
     */
    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns true if the account has expired
     * @return whether the account has expired
     */
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * Set whether the account has expired
     * @param accountNonExpired true if expired
     */
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    /**
     * Is the account enabled?
     * @return true if enabled
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Set whether the account is enabled
     * @param isEnabled
     */
    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * Returns the lastname of the user
     * @return lastname
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the users lastname
     * @param lastName lastname
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the firstname
     * @return firstaname
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the firstname
     * @param firstName firstname
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Return the user ID
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * Set the user ID
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns a String containing the user information
     * @return a String containing the user information
     */
    public String toString() {
        return firstName + " " + lastName + ":  " + "\nid: " + id + "\nfirstname: " + firstName + "\nlastname: " + lastName + "\nrfid: " + rfidKey.getId() + "\naccountNonExpired: " + accountNonExpired + "\nenabled: " + isEnabled;
    }

    /**
     * Gets the users RFID key
     * @return RFID key
     */
    public RfidKey getRfidKey() {
        return rfidKey;
    }

    /**
     * Set the users RFID key
     * @param rfidKey
     */
    public void setRfidKey(RfidKey rfidKey) {
        this.rfidKey = rfidKey;
    }

    /**
     * Create an Account from a LinkedHashMap.
     * @param accountMap The information about the account
     * @param encryptedUserCredentials The encrypted User credentials for this user
     */
    public void createAccountFromMap(LinkedHashMap<String, Object> accountMap, String encryptedUserCredentials) {
        firstName = (String) accountMap.get("firstName");
        lastName = (String) accountMap.get("lastName");
        username = (String) accountMap.get("username");
        id = (String) accountMap.get("id");
        accountNonExpired = (Boolean) accountMap.get("accountNonExpired");
        isEnabled = (Boolean) accountMap.get("enabled");
        rfidKey = new RfidKey((String) ((LinkedHashMap<String, Object>) accountMap.get("rfidKey")).get("id"));
        this.encryptedUserCredentials = encryptedUserCredentials;
    }

    /**
     * Returns the encrypted User credentials for the user
     * @return the encrypted User credentials for the user
     */
    public String getEncryptedUserCredentials() {
        return encryptedUserCredentials;
    }

    /**
     * Sets the encrypted User credentials for the user
     * @param encryptedUserCredentials the encrypted User credentials for the user
     */
    public void setEncryptedUserCredentials(String encryptedUserCredentials) {
        this.encryptedUserCredentials = encryptedUserCredentials;
    }

    /**
     * Returns the users username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the users username
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}

