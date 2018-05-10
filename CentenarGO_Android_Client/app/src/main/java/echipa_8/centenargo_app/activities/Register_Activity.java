package echipa_8.centenargo_app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.Register_Service;
import echipa_8.centenargo_core.utilities.ValidationUtility;

public class Register_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
    }

    public void submitRegisterAction(View view) {
        TextView tvEmail = findViewById(R.id.registerInputEmail);
        TextView tvUsername = findViewById(R.id.registerInputUsername);
        TextView tvPassword = findViewById(R.id.registerInputPassword);
        TextView tvRepeatPassword = findViewById(R.id.registerInputRepeatPassword);
        TextView tvFirstName = findViewById(R.id.registerInputFirstName);
        TextView tvLastName = findViewById(R.id.registerInputLastName);

        String email = tvEmail.getText().toString().trim();
        String username = tvUsername.getText().toString().trim();
        String password = tvPassword.getText().toString().trim();
        String repeatPass = tvRepeatPassword.getText().toString().trim();
        String firstName = tvFirstName.getText().toString().trim();
        String lastName = tvLastName.getText().toString().trim();

        if(!ValidationUtility.isValidEmail(email)){
            toast("Email address is not valid!");
            return;
        }

        if(!password.equals(repeatPass)){
            toast("Passwords don't match");
            return;
        }

        if(password.isEmpty())
        {
            toast("Password is required");
            return;
        }

        Register_Service register_service = new Register_Service(this);
        register_service.execute(email, username, password, firstName, lastName);
    }

    private void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void checkRegister(String response){
        if(null != response) {
            toast("SUCCESS");
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();
        } else {
            toast("FAIL - Email already exists!");
        }
    }
}
