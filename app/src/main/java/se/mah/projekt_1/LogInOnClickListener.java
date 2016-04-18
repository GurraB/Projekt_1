package se.mah.projekt_1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Gustaf on 13/04/2016.
 */
public class LogInOnClickListener implements View.OnClickListener {

    private Context context;
    private Controller controller;

    public LogInOnClickListener(Context context) {
        this.context = context;
        controller = new Controller();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_view_standard_login:
                controller.startNewActivity(context, StandardLogInActivity.class);
                break;
            case R.id.card_view_nfc_login:
                break;
            case R.id.card_view_nfc_check_in:
                break;
            default:
                return;
        }
    }
}
