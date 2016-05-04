package se.mah.projekt_1;

import android.content.Context;
import android.content.SharedPreferences;
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
    private AutoCompleteTextView etUsername;

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
        final SharedPreferences preferences = getSharedPreferences("users", MODE_PRIVATE);
        editor = preferences.edit();

        Object[] savedUserKeys = preferences.getAll().keySet().toArray();
        ArrayList<String> savedUsers = new ArrayList<>();
        for (int i = 0; i < savedUserKeys.length; i++)
            savedUsers.add(savedUserKeys[i].toString());

        etUsername.setThreshold(0);
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.equals(etUsername))
                    etUsername.showDropDown();
            }
        });
        etUsername.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, savedUsers));
        etUsername.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
                SharedPreferences prefs = getSharedPreferences(preferences.getString(((TextView) view).getText().toString(), null), MODE_PRIVATE);

                encryptedUserCredentials = prefs.getString("encryptedUserCredentials", null);
                String generatedPassword = "";
                for (int i = 0; i < prefs.getInt("passwordLength", 0); i++)
                    generatedPassword += "*";
                etPassword.setText(generatedPassword);
                cbRememberMe.setChecked(true);
                Log.v("encryptedUserCreds", encryptedUserCredentials);
                Log.v("generatedPassword", generatedPassword);
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(8);
        getSupportActionBar().setTitle("Log in");
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(this);
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.standard_log_in_app_bar);
        etPassword = (EditText) findViewById(R.id.etPassword);
        layout = (LinearLayout) findViewById(R.id.standard_login_layout);
        tvFade = (TextView) findViewById(R.id.login_fade);
        progressBar = (ProgressBar) findViewById(R.id.login_progressbar);
        cbRememberMe = (AppCompatCheckBox) findViewById(R.id.checkbox_remember_me);
        etUsername = (AutoCompleteTextView) findViewById(R.id.autoComplete);
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
        onBackPressed();
    }

    public void onLoginSuccess(Account user) {
        if(cbRememberMe.isChecked()) {
            editor.putString(user.getUsername(), String.valueOf(user.getUsername().hashCode()));
            editor.commit();
            SharedPreferences.Editor prefsEditor = getSharedPreferences(String.valueOf(user.getUsername().hashCode()), MODE_PRIVATE).edit();
            prefsEditor.putString("username", etUsername.getText().toString());
            prefsEditor.putInt("passwordLength", etPassword.getText().length());
            prefsEditor.putString("encryptedUserCredentials", user.getEncryptedUserCredentials());
            prefsEditor.putString("rfid", user.getRfidKey().getId());
            prefsEditor.commit();
        }
        SharedPreferences.Editor nfcPreferences = getSharedPreferences(user.getRfidKey().getId(), MODE_PRIVATE).edit();
        nfcPreferences.putString("encryptedUserCredentials", user.getEncryptedUserCredentials());
        nfcPreferences.commit();
        controller.startNewActivity(this, MainActivity.class, user);
        finish();
    }

    public void onLoginFail() {
        AlertDialog alertDialog = new LoginInFailDialog(this).create().create();
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
    }

    @Override
    public void showConnectionErrorMessage(String message, boolean retry) {
        Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
    }
}
