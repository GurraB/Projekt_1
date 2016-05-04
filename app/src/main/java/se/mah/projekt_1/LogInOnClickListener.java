package se.mah.projekt_1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Gustaf on 13/04/2016.
 */
public class LogInOnClickListener implements View.OnClickListener {

    private LogInActivity context;
    private Controller controller;

    public LogInOnClickListener(LogInActivity context) {
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
                controller.startNewActivity(context, NFCLogInActivity.class);
                break;
            case R.id.card_view_nfc_check_in:
                break;
            default:
                return;
        }
    }
}
