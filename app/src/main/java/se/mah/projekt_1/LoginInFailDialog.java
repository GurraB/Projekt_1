package se.mah.projekt_1;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Gustaf on 20/04/2016.
 */
public class LoginInFailDialog {

    private Context context;

    public LoginInFailDialog(Context context) {
        this.context = context;
    }

    public AlertDialog.Builder create(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("Login failed");
        alertDialogBuilder.setPositiveButton("OK", null);
        return alertDialogBuilder;
    }
}
