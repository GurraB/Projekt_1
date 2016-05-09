package se.mah.projekt_1;

import java.io.Serializable;

/**
 * Created by Gustaf on 05/05/2016.
 */
public class ScheduleStamp implements Serializable {

    private long from;
    private long to;
    private String rfid;

    public ScheduleStamp() {}

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }
}
