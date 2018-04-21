package echipa_8.centenargo_app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.Login_Service;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;
import echipa_8.centenargo_core.utilities.ValidationUtility;

import java.util.regex.Pattern;

public class Login_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        Context context = Login_Activity.this;
        SharedPreferences sharedPref = context.getSharedPreferences(SharedPreferencesUtility.sharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferencesUtility.setSharedPreferences(sharedPref);
    }

    public void registerAction(View view) {
        Intent intent = new Intent(getApplicationContext(), Register_Activity.class);
        startActivity(intent);
    }

    public void loginAction(View view) {
        TextView loginEmail = findViewById(R.id.inputLoginEmail);
        TextView loginPass = findViewById(R.id.inputLoginPassword);

        String email = loginEmail.getText().toString();
        String password = loginPass.getText().toString();

        if(!ValidationUtility.isValidEmail(email)){
            toast("Email address is not valid!");
            return;
        }

        Login_Service login_service = new Login_Service(this);
        login_service.execute(email, password);
    }

    private void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void loginComplete(String response){
        if(null != response){
            toast("SUCCESS");
            Intent intent = new Intent(getApplicationContext(), Routes_Activity.class);
            startActivity(intent);
            finish();
        } else {
            toast("FAIL");
        }
    }
}
