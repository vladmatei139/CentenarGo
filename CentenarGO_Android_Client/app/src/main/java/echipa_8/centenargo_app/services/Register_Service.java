package echipa_8.centenargo_app.services;

import android.os.AsyncTask;
import android.util.Log;

import echipa_8.centenargo_app.activities.Register_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class Register_Service extends AsyncTask<String, String, Object> {

    private WeakReference<Register_Activity> register_activity;

    public Register_Service(final Register_Activity register_activity) {
        this.register_activity = new WeakReference<>(register_activity);
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
            URL url = new URL("http://10.0.2.2:8080/api/signup");

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", strings[0]);
            jsonObject.put("username", strings[1]);
            jsonObject.put("password", strings[2]);
            jsonObject.put("firstname", strings[3]);
            jsonObject.put("lastname", strings[4]);


            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(jsonObject.toString());
            dos.flush();
            dos.close();

            Integer replyCode = httpURLConnection.getResponseCode();
            String replyMessage = httpURLConnection.getResponseMessage();

            if(replyCode == 200){
                Log.i("STATUS", String.valueOf(replyCode));
                Log.i("MESSAGE", replyMessage);
                Log.i("ACTIVITY", "New account created: " + strings[1]);
            } else {
                Log.w("ERROR", "Error code " + replyCode + ": " + replyMessage);
                return null;
            }

            return httpURLConnection.getResponseMessage();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        this.register_activity.get().checkRegister(null == o ? null : o.toString());
    }
}
