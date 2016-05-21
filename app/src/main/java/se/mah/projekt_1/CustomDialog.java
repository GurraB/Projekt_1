package se.mah.projekt_1;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Gustaf on 20/04/2016.
 * Builds a custom dialog
 */
public class CustomDialog {

    private Context context;

    /**
     * Constructor
     * @param context where the dialog will be shown
     */
    public CustomDialog(Context context) {
        this.context = context;
    }

    /**
     * Creates an AlertDialog
     * @param title the title in the dialog
     * @param message the message in the dialog
     * @return An AlertDialogBuilder
     */
    public AlertDialog.Builder create(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("OK", null);
        return alertDialogBuilder;
    }
}
