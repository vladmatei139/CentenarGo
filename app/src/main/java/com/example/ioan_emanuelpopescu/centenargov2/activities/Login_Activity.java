package com.example.ioan_emanuelpopescu.centenargov2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ioan_emanuelpopescu.centenargov2.R;
import com.example.ioan_emanuelpopescu.centenargov2.services.Login_Service;

import java.util.regex.Pattern;

public class Login_Activity extends AppCompatActivity {

    public static String response = "";

    public static final Pattern emailAddressPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
    }

    public void registerAction(View view) {
        Intent intent = new Intent(getApplicationContext(), Register_Activity.class);
        startActivity(intent);
    }

    public void loginAction(View view) {

        TextView loginEmail = findViewById(R.id.inputLoginEmail);
        TextView loginPass = findViewById(R.id.inputLoginPassword);

        CharSequence email = loginEmail.getText();
        CharSequence password = loginPass.getText();

        if(!isValidEmail("")){
            toast("Email address is not valid!");
            return;
        }

        Login_Service login_service = new Login_Service();
        login_service.execute(email.toString(), password.toString());

        if(null != getResponse()){
            toast("SUCCESS");
            Intent intent = new Intent(getApplicationContext(), Routes_Activity.class);
            startActivity(intent);
        } else {
            toast("FAIL");
        }


    }

    private Boolean isValidEmail(String emailAddress){
        return true;
    }

    private void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    public static void setResponse(String newResponse){
        response = newResponse;
    }
    public static String getResponse(){
        return response;
    }
}
