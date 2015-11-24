package com.tourismkkc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityLoginActivity extends Activity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstances();

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

    }

    private void initInstances() {

        editTextEmail = (EditText) findViewById(R.id.login_edit_email);
        editTextPassword = (EditText) findViewById(R.id.login_edit_password);
        buttonLogin = (Button) findViewById(R.id.login_btn_login);
        buttonRegister = (Button) findViewById(R.id.login_btn_register);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_btn_login:
                break;
            case R.id.login_btn_register:
                Intent intent = new Intent(getApplicationContext(), ActivityRegisterActivity.class);
                startActivity(intent);
                break;

        }

    }
}
