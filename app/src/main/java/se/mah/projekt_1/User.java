package se.mah.projekt_1;

import java.io.Serializable;

/**
 * Created by Gustaf on 11/04/2016.
 */
public class User implements Serializable {

    private String firstName;
    private String lastName;
    private RfidKey rfid;
    private String id;

    public User() {}

    public User(String[] userInfo) {
        this.firstName = userInfo[0];
        this.lastName = userInfo[1];
        this.rfid = new RfidKey(userInfo[2]);
        this.id = userInfo[3];
    }

    public User(String firstName, String lastName, RfidKey key, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.rfid = key;
        this.id = id;
    }

    public User(String fistname, String lastName) {
        this.firstName = fistname;
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public RfidKey getRfid() {
        return rfid;
    }

    public void setRfid(RfidKey rfid) {
        this.rfid = rfid;
    }

    public String[] toStringArray() {
        return new String[]{firstName, lastName, rfid.getId(), id};
    }
}
