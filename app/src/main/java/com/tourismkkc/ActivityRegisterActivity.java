package com.tourismkkc;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityRegisterActivity extends Activity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword, editTextConfirm, editTextFirst, editTextLast;
    private Button buttonRegister, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initial();
        initInstances();
        buttonRegister.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
    }

    private void initial() {
        setContentView(R.layout.activity_register);
    }

    private void initInstances() {
        editTextEmail = (EditText) findViewById(R.id.register_edit_email);
        editTextPassword = (EditText) findViewById(R.id.register_edit_password);
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
                if (getStrEmail().isEmpty() || getStrPassword().isEmpty() || getStrConfirm().isEmpty() || getStrFirst().isEmpty() || getStrLast().isEmpty()) {
                    /* isEmpty = true */
                    Toast.makeText(getApplicationContext(), "isEmpty: TRUE", Toast.LENGTH_LONG).show();
                } else {
                    /* isEmpty = false */
                    if (getStrPassword().equals(getStrConfirm())) {
                        Toast.makeText(getApplicationContext(), "PASSWORD: TRUE", Toast.LENGTH_LONG).show();
                        //Connect API
                        new LoadAPI().execute(new DataRegister(strEmail, strPassword, strFirst, strLast));
                    } else {
                        Toast.makeText(getApplicationContext(), "PASSWORD: FALSE", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.register_btn_back:
                startActivity(new Intent(getApplicationContext(), ActivityLoginActivity.class));
                break;
        }
    }

    private void editStr() {
        setStrEmail(editTextEmail.getText().toString());
        setStrPassword(editTextPassword.getText().toString());
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

    class LoadAPI extends AsyncTask<DataRegister, Void, APIStatus> {

        private APIConnect apiConnect = new APIConnect();
        private APIStatus apiStatus = new APIStatus();

        @Override
        protected APIStatus doInBackground(DataRegister... params) {
            apiStatus = apiConnect.register(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(APIStatus result) {
            Toast.makeText(getApplicationContext(), apiStatus.getReason(), Toast.LENGTH_LONG).show();
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                startActivity(new Intent(getApplicationContext(), ActivityLoginActivity.class));
            }
        }
    }
}
