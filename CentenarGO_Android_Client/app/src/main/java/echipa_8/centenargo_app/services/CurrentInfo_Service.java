package echipa_8.centenargo_app.services;

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

import echipa_8.centenargo_app.activities.Routes_Activity;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

public class CurrentInfo_Service extends AsyncTask<String, String, Object> {

    private WeakReference<Routes_Activity> routes_activity;
    private String newRoute;
    private int currentCompleted;
    private int newCompleted;

    public CurrentInfo_Service(final Routes_Activity routes_activity) {
        this.routes_activity = new WeakReference<>(routes_activity);
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
            String token = SharedPreferencesUtility.getToken();

            URL url = new URL("http://10.0.2.2:8080/api/getCurrentInfo/" + strings[0]);

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            newRoute = strings[0];
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(jsonObject.toString());
            dos.flush();
            dos.close();

            Integer replyCode = httpURLConnection.getResponseCode();
            String replyMessage = httpURLConnection.getResponseMessage();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            if ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject replyJSON = new JSONObject(response.toString());

            StringBuilder sb = new StringBuilder();

            String currentRoute = replyJSON.getString("currentroute");
            currentCompleted = replyJSON.getInt("currentCompleted");
            newCompleted = replyJSON.getInt("newCompleted");
            sb.append(currentRoute);

            String currentroute = sb.toString();
            if (replyCode == 200) {
                Log.i("STATUS", replyCode.toString());
                Log.i("MESSAGE", replyMessage);
            } else {
                Log.w("ERROR", "Error code " + replyCode + ": " + replyMessage);
                return null;
            }

            return currentroute;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        this.routes_activity.get().setRoute(newRoute, currentCompleted, newCompleted, null != o ? o.toString() : "null");
    }
}
