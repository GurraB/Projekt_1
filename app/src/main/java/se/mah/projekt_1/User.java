package se.mah.projekt_1;

/**
 * Created by Gustaf on 11/04/2016.
 */
public class User {
    private String fistname;
    private String lastname;
    private RfidKey key;
    private String id;

    public User(String fistname, String lastname, RfidKey key, String id) {
        this.fistname = fistname;
        this.lastname = lastname;
        this.key = key;
        this.id = id;
    }

    public User(String fistname, String lastname) {
        this.fistname = fistname;
        this.lastname = lastname;
    }

    public String getName() {
        return fistname + " " + lastname;
    }

    public void setKey(RfidKey key) {
        this.key = key;
    }

    public RfidKey getKey() {
        return key;
    }

    public String getFistname() {
        return fistname;
    }

    public void setFistname(String fistname) {
        this.fistname = fistname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
