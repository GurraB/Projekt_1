package se.mah.projekt_1.Services;

import android.os.AsyncTask;

import se.mah.projekt_1.Controller.Controller;

/**
 * Created by Gustaf Bohlin on 24/05/2016.
 * Asyncronous call to server to log out
 */
public class LogoutService extends AsyncTask<String, String, String> {

    private Controller controller;

    /**
     * Constructor
     * @param controller the controller being used
     */
    public LogoutService(Controller controller) {
        this.controller = controller;
    }

    /**
     * Calls the server to logout
     * @param strings
     * @return nothing
     */
    @Override
    protected String doInBackground(String... strings) {
        new ServerCommunicationService(controller).logout(strings[0]);
        return "";
    }
}
