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
    private ImageView sharedElement1;
    private ImageView sharedElement2;
    private ImageView sharedElement3;

    public LogInOnClickListener(LogInActivity context, ImageView sharedElement1, ImageView sharedElement2, ImageView sharedElement3) {
        this.context = context;
        controller = new Controller();
        this.sharedElement1 = sharedElement1;
        this.sharedElement2 = sharedElement2;
        this.sharedElement3 = sharedElement3;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_view_standard_login:
                Intent intent = new Intent(context, StandardLogInActivity.class);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context, new Pair<View, String>(
                                view.findViewById(R.id.shared_element1),
                                context.getString(R.string.transition1)));

                context.startActivity(intent, options.toBundle());
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
