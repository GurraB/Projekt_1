package se.mah.projekt_1;

import java.io.Serializable;

/**
 * Created by Gustaf on 07/04/2016.
 */
public class RfidKey implements Serializable {
    private String id;

    public RfidKey() {}

    public RfidKey(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
