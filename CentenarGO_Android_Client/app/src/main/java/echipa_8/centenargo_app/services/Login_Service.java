package echipa_8.centenargo_app.services;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import echipa_8.centenargo_app.activities.Login_Activity;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

//  Params, Progress, Result
public class Login_Service extends AsyncTask<String, String, Object> {

    private WeakReference<Login_Activity> login_activity;
    private static String token = null;

    public Login_Service(final Login_Activity login_activity) {
        this.login_activity = new WeakReference<>(login_activity);
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
<<<<<<< HEAD

=======
>>>>>>> 2439c3475512d04437f97e2399f3bedde85f4aa6
            URL url = new URL("http://10.0.2.2:8080/api/login");

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", strings[0]);
            jsonObject.put("password", strings[1]);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(jsonObject.toString());
            dos.flush();
            dos.close();

            Integer replyCode = httpURLConnection.getResponseCode();
            String replyMessage = httpURLConnection.getResponseMessage();


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject replyJSON = new JSONObject(response.toString());


            if(replyCode == 200){
                Log.i("STATUS", replyCode.toString());
                Log.i("MESSAGE", replyMessage);
                token = replyJSON.getString("token");
                SharedPreferencesUtility.putUserDetails(token, strings[0]);
            } else {
                Log.w("ERROR", "Error code " + replyCode + ": " + replyMessage);
                return null;
            }

            return replyMessage;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        this.login_activity.get().loginComplete(null == o ? null : o.toString());
    }
}
