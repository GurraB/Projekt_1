package se.mah.projekt_1;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class NFCLogInActivity extends AppCompatActivity implements AsyncTaskCompatible {

    private Controller controller;
    private TextView textView;
    private NfcAdapter nfcAdapter;
    private String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(),
                    Ndef.class.getName()
            }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfclog_in);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, techList);
        Log.v("NFCLOGINACTIVITY", getIntent().getAction());
        Intent intent = getIntent();
        if(intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String tagID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            Log.v("NFC TAG", tagID);
            Log.v("NFC TAG", tagID);
            logInWithRfid(tagID);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String tagID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            Log.v("NFC TAG", tagID);
            logInWithRfid(tagID);
        }
    }

    private void logInWithRfid(String tagID) {
        controller = new Controller(this);
        SharedPreferences preferences = getSharedPreferences("userdata", MODE_PRIVATE);
        String encryptedUserCredentials = preferences.getString(tagID, null);
        if(encryptedUserCredentials != null)
            controller.login(encryptedUserCredentials);
        else {
            Log.v("NFCLOGINACTIVITY", "User not Found!!!!");
            //TODO open dialog
        }
    }

    private String ByteArrayToHexString(byte[] inArray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for(i = 0; i < inArray.length; ++i) {
            in = (int) inArray[i];
            j = (in >> 4) & 0x0F;
            out += hex[j];
            j = in & 0x0F;
            out += hex[j];
        }
        return out;
    }

    public void onLoginSuccess(Account user) {
        controller.startNewActivity(this, MainActivity.class, user);
    }

    @Override
    public void onLoginFail() {
        Log.v("NFCLOGINACTIVITY", "LOGIN FAILED");
        //TODO SnackBar
    }

    @Deprecated
    @Override
    public void dataRecieved(ArrayList<AndroidStamp> stamps) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void startLoadingAnimation() {
        //TODO start loading
    }

    @Override
    public void stopLoadingAnimation() {
        //TODO stop Loading
    }

    @Override
    public void showConnectionErrorMessage(String message, boolean retry) {

    }
}
