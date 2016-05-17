package se.mah.projekt_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StandardLogInActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompatible{

    private Toolbar toolbar;
    private EditText etPassword;
    private MenuItem logInButton;
    private Controller controller;
    private LinearLayout layout;
    private TextView tvFade;
    private ProgressBar progressBar;
    private AppCompatCheckBox cbRememberMe;
    private String encryptedUserCredentials = "";
    private SharedPreferences.Editor editor;
    private EditText etUsername;
    private ImageView ivNfcAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_log_in);
        controller = new Controller(this);
        initComponents();
        setUpToolbar();
        setUpRememberMe();
    }

    private void setUpRememberMe() {
        SharedPreferences preferences = getSharedPreferences("users", MODE_PRIVATE);
        editor = preferences.edit();

        encryptedUserCredentials = preferences.getString("currentUser", "");
        if(encryptedUserCredentials != "" && getIntent().getAction() != null)
            controller.login(encryptedUserCredentials);
        encryptedUserCredentials = "";
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(8);
        getSupportActionBar().setTitle("Log in");
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.standard_log_in_app_bar);
        etPassword = (EditText) findViewById(R.id.etPassword);
        layout = (LinearLayout) findViewById(R.id.standard_login_layout);
        tvFade = (TextView) findViewById(R.id.login_fade);
        progressBar = (ProgressBar) findViewById(R.id.login_progressbar);
        cbRememberMe = (AppCompatCheckBox) findViewById(R.id.checkbox_remember_me);
        etUsername = (EditText) findViewById(R.id.etUsername);
        ivNfcAnimation = (ImageView) findViewById(R.id.ivNfcLoginAnimation);
        AnimationDrawable nfcAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.nfc_animation_2);
        ivNfcAnimation.setImageDrawable(nfcAnimation);
        nfcAnimation.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.standard_log_in_dialog_menu, menu);
        logInButton = menu.findItem(R.id.dialog_logIn);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(logInButton)) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
            if(encryptedUserCredentials == "")
                controller.login(etUsername.getText().toString(), etPassword.getText().toString());
            else
                controller.login(encryptedUserCredentials);
            item.setEnabled(false);
            toolbar.setNavigationOnClickListener(null);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        //onBackPressed();
        if(encryptedUserCredentials == "")
            controller.login(etUsername.getText().toString(), etPassword.getText().toString());
        else
            controller.login(encryptedUserCredentials);
    }

    public void onLoginSuccess(Account user) {
        if(cbRememberMe.isChecked()) {
            editor.putString("currentUser", user.getEncryptedUserCredentials());
            editor.commit();
        }
        SharedPreferences.Editor nfcPreferences = getSharedPreferences(user.getRfidKey().getId(), MODE_PRIVATE).edit();
        nfcPreferences.putString("encryptedUserCredentials", user.getEncryptedUserCredentials());
        nfcPreferences.commit();
        controller.startNewActivity(this, MainActivity.class, user);
        finish();
    }

    public void onLoginFail() {
        AlertDialog alertDialog = new LoginInFailDialog(this).create("Wrong username or password").create();
        alertDialog.show();
        logInButton.setEnabled(true);
        toolbar.setNavigationOnClickListener(this);
    }

    @Deprecated
    @Override
    public void dataRecieved(ArrayList<AndroidStamp> stamps) {throw new UnsupportedOperationException();}

    @Override
    public void startLoadingAnimation() {
        progressBar.setVisibility(View.VISIBLE);
        tvFade.setVisibility(View.VISIBLE);
    }

    public void stopLoadingAnimation() {
        progressBar.setVisibility(View.GONE);
        tvFade.setVisibility(View.GONE);
        logInButton.setEnabled(true);
    }

    @Override
    public void showConnectionErrorMessage(Controller.ErrorType type, String message) {
        switch (type) {
            case CONNECTION_ERROR:
                Snackbar snackbar = Snackbar.make(layout, "Unable to connect to server", Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", this);
                snackbar.show();
                break;
            case UNATHORIZED:
                onLoginFail();
                break;
            case BAD_REQUEST:
                Snackbar.make(layout, "Bad request", Snackbar.LENGTH_LONG).show();
                break;
            default:
                Snackbar.make(layout, "Unknown error", Snackbar.LENGTH_LONG).show();
                break;
        }
    }
}
