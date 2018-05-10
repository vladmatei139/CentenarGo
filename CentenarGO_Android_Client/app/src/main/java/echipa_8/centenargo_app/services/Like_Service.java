package echipa_8.centenargo_app.services;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

/**
 * Created by A6I3M2 on 10-May-18.
 */

public class Like_Service extends AsyncTask<String, String, Object> {

    public Like_Service() {
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
            URL url = new URL("http://10.0.2.2:8080/api/gallery/" + strings[0] + "/addLike");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/java");
            httpURLConnection.connect();

            String token = SharedPreferencesUtility.getToken();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(jsonObject.toString());
            dos.flush();
            dos.close();

            Integer replyCode = httpURLConnection.getResponseCode();
            String replyMessage = httpURLConnection.getResponseMessage();

            return replyCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}