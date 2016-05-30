package se.mah.projekt_1.Services;

import android.os.AsyncTask;

import se.mah.projekt_1.Controller.Controller;
import se.mah.projekt_1.Models.Account;

/**
 * Created by Gustaf Bohlin on 27/04/2016.
 * Handles the login
 */
public class LoginService extends AsyncTask<String, String, Account> {

    private Controller controller;
    private String url = "https://projektessence.se/api/account";

    /**
     * Constructor
     * @param controller the controller
     */
    public LoginService(Controller controller) {
        this.controller = controller;
    }

    /**
     * Sends the request to the server and returns the user
     * @param strings [username], [password] or [encryptedUserCredentials]
     * @return the user logged in as
     */
    @Override
    protected Account doInBackground(String... strings) {
        if(strings.length == 2)
            return new ServerCommunicationService(controller).login(url, strings[0], strings[1]);
        return new ServerCommunicationService(controller).login(url, strings[0]);
    }

    /**
     * When the user has been retrieved from the server it sends it to the controller
     * @param account the retrieved user
     */
    @Override
    protected void onPostExecute(Account account) {
        super.onPostExecute(account);
        controller.finishedLoading(account);
    }
}
