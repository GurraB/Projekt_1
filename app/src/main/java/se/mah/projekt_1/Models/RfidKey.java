package se.mah.projekt_1.Models;

import java.io.Serializable;

/**
 * Created by Gustaf Bohlin on 07/04/2016.
 * Represents the key of an RFID card
 */
public class RfidKey implements Serializable {
    private String id;

    /**
     * Empty constructor for serializing
     */
    public RfidKey() {}

    /**
     * Constructor
     * @param id the id of the card
     */
    public RfidKey(String id) {
        this.id = id;
    }

    /**
     * Returns the cards ID
     * @return the cards ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the card
     * @param id the ID of the card
     */
    public void setId(String id) {
        this.id = id;
    }
}
