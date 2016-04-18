package se.mah.projekt_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

public class LogInActivity extends AppCompatActivity {

    private LogInOnClickListener onClickListener;
    private CardView cardViewuserCredentials;
    private CardView cardViewNFCCard;
    private CardView cardViewNFCStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        cardViewuserCredentials = (CardView) findViewById(R.id.card_view_standard_login);
        cardViewNFCCard = (CardView) findViewById(R.id.card_view_nfc_login);
        cardViewNFCStamp = (CardView) findViewById(R.id.card_view_nfc_check_in);

        onClickListener = new LogInOnClickListener(this);

        cardViewuserCredentials.setOnClickListener(onClickListener);
        cardViewNFCCard.setOnClickListener(onClickListener);
        cardViewNFCStamp.setOnClickListener(onClickListener);
    }
}
