package se.mah.projekt_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StandardLogInActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompatible{

    private Toolbar toolbar;
    private EditText etUsername;
    private EditText etPassword;
    private MenuItem logInButton;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_log_in);
        controller = new Controller(this);
        initComponents();
        setUpToolbar();
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
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
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
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            Log.v("USERNAME", username);
            Log.v("PASSWORD", password);
            controller.login(username, password);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    public void onLoginSuccess() {

    }

    public void onLoginFail() {

    }

    public void stopLoadingAnimation() {

    }

    @Override
    public void dataRecieved(Object data) {
        controller.parseLoginInformation((ArrayList<LinkedHashMap<String, Object>>) data);
    }
}
