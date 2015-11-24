package com.tourismkkc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

                editStr();

                if (getStrEmail().isEmpty()) {
/*false*/
                    Toast.makeText(getApplicationContext(), "FALSE", Toast.LENGTH_LONG).show();
                } else {
/*true*/
                    Toast.makeText(getApplicationContext(), "TRUE", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.register_btn_back:
                Intent intent = new Intent(getApplicationContext(), ActivityLoginActivity.class);
                startActivity(intent);
                break;

        }

    }

    private void editStr() {

        setStrEmail(editTextEmail.getText().toString());
        setStrPassword(editTextpassword.getText().toString());
        setStrConfirm(editTextConfirm.getText().toString());
        setStrFirst(editTextFirst.getText().toString());
        setStrLast(editTextLast.getText().toString());

    }

    private String strEmail, strPassword, strConfirm, strFirst, strLast;

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public String getStrConfirm() {
        return strConfirm;
    }

    public void setStrConfirm(String strConfirm) {
        this.strConfirm = strConfirm;
    }

    public String getStrFirst() {
        return strFirst;
    }

    public void setStrFirst(String strFirst) {
        this.strFirst = strFirst;
    }

    public String getStrLast() {
        return strLast;
    }

    public void setStrLast(String strLast) {
        this.strLast = strLast;
    }
}
