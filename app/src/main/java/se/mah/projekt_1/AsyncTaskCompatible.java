package se.mah.projekt_1;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Gustaf on 14/04/2016.
 */
public interface AsyncTaskCompatible {
    void dataRecieved(ArrayList<AndroidStamp> stamps);
    void startLoadingAnimation();
    void stopLoadingAnimation();
    void showConnectionErrorMessage(String message, boolean retry);
}
