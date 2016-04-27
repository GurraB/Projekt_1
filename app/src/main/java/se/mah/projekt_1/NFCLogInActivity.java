package se.mah.projekt_1;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NFCLogInActivity extends AppCompatActivity {

    TextView textView;
    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfclog_in);

        textView = (TextView) findViewById(R.id.textViewNFC);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(nfcAdapter != null && nfcAdapter.isEnabled())
            Log.v("NFCACTIVITY", "nfc is enabled");
        else
            Log.v("NFCACTIVITY", "nfc is NOT enabled");

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if ("text/plain".equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                Log.v("NFCACTIVITY", "READ CARD");
            } else
                Log.v("NFCLOGINACTIVITY", "DIS MIMETYPE IS WRONG: " + type);
        }
        else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if(searchedTech.equals(tech)) {
                    Log.v("NFCACTIVITY", "Read card");
                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpForegroundDispatch(this, nfcAdapter);
    }

    public static void setUpForegroundDispatch(NFCLogInActivity nfcLogInActivity, NfcAdapter adapter) {
        final Intent intent = new Intent(nfcLogInActivity.getApplicationContext(), nfcLogInActivity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(nfcLogInActivity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Log.v("NFCACTIVITY", "Check your mime type mister");
            e.printStackTrace();
        }

        adapter.enableForegroundDispatch(nfcLogInActivity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final NFCLogInActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopForegroundDispatch(this, nfcAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
