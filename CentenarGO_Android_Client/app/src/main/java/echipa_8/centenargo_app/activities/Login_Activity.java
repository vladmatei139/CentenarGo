package echipa_8.centenargo_app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.Login_Service;

import java.util.regex.Pattern;

public class Login_Activity extends AppCompatActivity {

    public static final Pattern emailAddressPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    private static String token = null;

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

        String email = loginEmail.getText().toString();
        String password = loginPass.getText().toString();

        if(!isValidEmail(email)){
            toast("Email address is not valid!");
            return;
        }

        Login_Service login_service = new Login_Service(this);
        login_service.execute(email, password);
    }

    private Boolean isValidEmail(String emailAddress){
        return emailAddressPattern.matcher(emailAddress).matches();
    }

    private void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void setToken(String t){
        token = t;
    }

    public void loginComplete(String response){
        if(null != response){
            toast("SUCCESS");
            Intent intent = new Intent(getApplicationContext(), Routes_Activity.class);
            intent.putExtra("TOKEN", token);
            startActivity(intent);
        } else {
            toast("FAIL");
        }
    }
}
