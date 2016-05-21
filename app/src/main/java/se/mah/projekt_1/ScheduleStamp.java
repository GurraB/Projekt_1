package se.mah.projekt_1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * Created by Gustaf on 05/05/2016.
 */
@JsonDeserialize(using = ScheduleStampSerializer.class)
public class ScheduleStamp implements Serializable {

    private long from;
    private long to;
    private String[] userId;

    public ScheduleStamp() {}

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

    public String[] getUserId() {
        return userId;
    }

    public void setUserId(String[] userId) {
        this.userId = userId;
    }
}
