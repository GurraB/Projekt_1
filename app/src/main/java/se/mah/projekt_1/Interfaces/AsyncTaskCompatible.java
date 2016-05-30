package se.mah.projekt_1.Interfaces;

import java.util.ArrayList;

import se.mah.projekt_1.Controller.Controller;
import se.mah.projekt_1.Models.Account;
import se.mah.projekt_1.Models.AndroidStamp;

/**
 * Created by Gustaf Bohlin on 14/04/2016.
 * An interface for the activities that connects to server
 */
public interface AsyncTaskCompatible {
    /**
     * Updates the view with the new timestamps
     * @param stamps the new timestamps
     */
    void dataRecieved(ArrayList<AndroidStamp> stamps);

    /**
     * Start the loading animation
     */
    void startLoadingAnimation();

    /**
     * Stop the loading animation
     */
    void stopLoadingAnimation();

    /**
     * Show a connectionerror message
     * @param type What type of error
     * @param message The message to be displayed
     */
    void showConnectionErrorMessage(Controller.ErrorType type, String message);

    /**
     * Proceedes if the login was successful
     * @param user User logged in as
     */
    void onLoginSuccess(Account user);

    /**
     * Handle failed login
     */
    void onLoginFail();
}
