package se.mah.projekt_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LogInActivity extends AppCompatActivity {

    private LogInOnClickListener onClickListener;
    private CardView cardViewuserCredentials;
    private CardView cardViewNFCCard;
    private CardView cardViewNFCStamp;
    private ImageView sharedElement1;
    private ImageView sharedElement2;
    private ImageView sharedElement3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        new LoginServiceTest(this).execute("user", "pass");

        Animation slideInAnim1 = AnimationUtils.loadAnimation(this, R.anim.start_screen_animation);
        Animation slideInAnim2 = AnimationUtils.loadAnimation(this, R.anim.start_screen_animation);
        Animation slideInAnim3 = AnimationUtils.loadAnimation(this, R.anim.start_screen_animation);
        slideInAnim2.setStartOffset(100);
        slideInAnim3.setStartOffset(200);

        cardViewuserCredentials = (CardView) findViewById(R.id.card_view_standard_login);
        cardViewNFCCard = (CardView) findViewById(R.id.card_view_nfc_login);
        cardViewNFCStamp = (CardView) findViewById(R.id.card_view_nfc_check_in);

        sharedElement1 = (ImageView) findViewById(R.id.shared_element1);
        sharedElement2 = (ImageView) findViewById(R.id.shared_element2);
        sharedElement3 = (ImageView) findViewById(R.id.shared_element3);

        cardViewuserCredentials.startAnimation(slideInAnim1);
        cardViewNFCCard.startAnimation(slideInAnim2);
        cardViewNFCStamp.startAnimation(slideInAnim3);

        onClickListener = new LogInOnClickListener(this, sharedElement1, sharedElement2, sharedElement3);

        cardViewuserCredentials.setOnClickListener(onClickListener);
        cardViewNFCCard.setOnClickListener(onClickListener);
        cardViewNFCStamp.setOnClickListener(onClickListener);
    }

    public void finishedLoading(String res) {
        if(res == null)
            Log.v("FINISHEDlOADING", "res is null");
        else
            Log.v("RESULT", res);
    }
}
