package com.tourismkkc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityRegisterActivity extends Activity implements View.OnClickListener {

    private EditText editTextEmail, editTextpassword, editTextConfirm, editTextFirst, editTextLast;
    private Button buttonRegister, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initInstances();

        buttonRegister.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

    }

    private void initInstances() {

        editTextEmail = (EditText) findViewById(R.id.register_edit_email);
        editTextpassword = (EditText) findViewById(R.id.register_edit_password);
        editTextConfirm = (EditText) findViewById(R.id.register_edit_confirm);
        editTextFirst = (EditText) findViewById(R.id.register_edit_first);
        editTextLast = (EditText) findViewById(R.id.register_edit_last);
        buttonRegister = (Button) findViewById(R.id.register_btn_register);
        buttonBack = (Button) findViewById(R.id.register_btn_back);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.register_btn_register:
                break;
            case R.id.register_btn_back:
                Intent intent = new Intent(getApplicationContext(), ActivityLoginActivity.class);
                startActivity(intent);
                break;

        }

    }
}
