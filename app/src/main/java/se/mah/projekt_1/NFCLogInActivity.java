package se.mah.projekt_1;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gustaf on 27/04/2016.
 * Reads a RFID card and logs in using the RFID cards UID
 */

public class NFCLogInActivity extends AppCompatActivity implements AsyncTaskCompatible {

    private Controller controller;
    private ProgressBar progressBar;
    private TextView tvFade;
    private ImageView ivNfcAnimation;
    private AnimationDrawable nfcAnimation;
    private Toolbar toolbar;
    private NfcAdapter nfcAdapter;
    private boolean tagIsSaved;
    private String scannedTagId;
    private AlertDialog loginDeniedDialog;
    private AlertDialog wrongUserDialog;
    private String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(),
                    Ndef.class.getName()
            }
    };


    /**
     * OnCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfclog_in);
    }

    private void initAppBarAndIv() {
        ivNfcAnimation = (ImageView) findViewById(R.id.ivNfcAnimation);
        nfcAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.nfc_animation_2);
        ivNfcAnimation.setImageDrawable(nfcAnimation);
        nfcAnimation.start();
        toolbar = (Toolbar) findViewById(R.id.app_bar_nfc);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(8);
        getSupportActionBar().setTitle("Log in using card");
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * When the activity is resumed it enables reading the RFID card and does that
     */
    @Override
    protected void onResume() {
        super.onResume();
        initAppBarAndIv();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, techList);
        Intent intent = getIntent();
        if (intent.getAction() != null) {
            if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
                String tagID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                scannedTagId = tagID;
                Log.v("NFC TAG", tagID);
                logInWithRfid(tagID);
            }
        }
    }

    /**
     * When paused disables the reading of the RFID cards UID
     */
    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
        nfcAnimation = null;
        if(wrongUserDialog != null)
            if(wrongUserDialog.isShowing()) {
                wrongUserDialog.setOnDismissListener(null);
                wrongUserDialog.dismiss();
            }
        if(loginDeniedDialog != null)
            if(loginDeniedDialog.isShowing())
                loginDeniedDialog.dismiss();
    }

    /**
     * onNewIntent when the activity is started from the same activity read the card again
     * @param intent Intent that contains information about the scanned card
     */
    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String tagID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            scannedTagId = tagID;
            Log.v("NFC TAG", tagID);
            logInWithRfid(tagID);
        }
    }

    private void logInWithRfid(String tagID) {
        controller = new Controller(this);
        SharedPreferences preferences = getSharedPreferences(tagID, MODE_PRIVATE);
        String encryptedUserCredentials = preferences.getString("encryptedUserCredentials", null);
        if (encryptedUserCredentials != null) {
            tagIsSaved = true;
            controller.login(encryptedUserCredentials);
        } else {
            tagIsSaved = false;
            showDeniedDialog();
        }
    }

    private void showDeniedDialog() {
        View rootView = LayoutInflater.from(this).inflate(R.layout.dialog_nfc_denied_login, null);
        final EditText etUsername = (EditText) rootView.findViewById(R.id.etUsername_dialog);
        final EditText etPassword = (EditText) rootView.findViewById(R.id.etPassword_dialog);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(rootView);
        alertDialogBuilder.setTitle("Denied");
        alertDialogBuilder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                controller.login(etUsername.getText().toString(), etPassword.getText().toString());
            }
        });
        loginDeniedDialog = alertDialogBuilder.create();
        loginDeniedDialog.show();
    }

    private String ByteArrayToHexString(byte[] inArray) {
        int i, hexValue, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (i = 0; i < inArray.length; ++i) {
            in = (int) inArray[i];
            hexValue = (in >> 4) & 0x0F;
            out += hex[hexValue];
            hexValue = in & 0x0F;
            out += hex[hexValue];
        }
        return out;
    }

    @Override
    public void onLoginSuccess(Account user) {
        if (!tagIsSaved && scannedTagId == user.getRfidKey().getId()) {
            getSharedPreferences(user.getRfidKey().getId(), MODE_PRIVATE).edit().putString("encryptedUserCredentials", user.getEncryptedUserCredentials()).commit();
            controller.startNewActivity(this, MainActivity.class, user);
            finish();
        }
        else
            showWrongCardDialog(user);
    }

    private void dismiss(Account user) {
        controller.startNewActivity(this, MainActivity.class, user);
        finish();
    }

    private void showWrongCardDialog(final Account user) {
        wrongUserDialog = new CustomDialog(this).create("Wrong user", "Scanned card is not paired with your user, you will be logged in as " + user.getFirstName() + " " + user.getLastName()).create();
        wrongUserDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dismiss(user);
            }
        });
        wrongUserDialog.show();
    }

    @Override
    public void onLoginFail() {
        AlertDialog alertDialog = new CustomDialog(this).create("Login Failed", "Wrong username or password").create();
        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                showDeniedDialog();
            }
        });
    }

    @Deprecated
    @Override
    public void dataRecieved(ArrayList<AndroidStamp> stamps) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void startLoadingAnimation() {
        if (tvFade == null || progressBar == null)
            initComponents();
        tvFade.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initComponents() {
        tvFade = (TextView) findViewById(R.id.textViewNfcFade);
        progressBar = (ProgressBar) findViewById(R.id.nfc_progressbar);
    }

    @Override
    public void stopLoadingAnimation() {
        if (tvFade == null || progressBar == null)
            initComponents();
        tvFade.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showConnectionErrorMessage(Controller.ErrorType type, String message) {
        switch (type) {
            case CONNECTION_ERROR:
                Snackbar snackbar = Snackbar.make(toolbar, "Unable to connect to server", Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                snackbar.show();
                break;
            case UNATHORIZED:
                onLoginFail();
                break;
            case BAD_REQUEST:
                Snackbar.make(toolbar, "Bad request", Snackbar.LENGTH_LONG).show();
                break;
            default:
                Snackbar.make(toolbar, "Unknown error", Snackbar.LENGTH_LONG).show();
                break;
        }
    }
}
