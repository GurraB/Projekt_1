package se.mah.projekt_1;

import java.io.Serializable;

/**
 * Created by Gustaf on 02/05/2016.
 */
public class SessionInformation implements Serializable {
    private String remoteAddress;
    private int sessionId;

    public String toString() {
        return "RemoteAddress " + remoteAddress + "| SessionId " + sessionId;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
