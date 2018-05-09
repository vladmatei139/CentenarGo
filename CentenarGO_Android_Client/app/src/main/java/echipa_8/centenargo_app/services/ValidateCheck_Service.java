package echipa_8.centenargo_app.services;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import echipa_8.centenargo_app.activities.Landmark_Activity;
import echipa_8.centenargo_app.activities.Questions_Activity;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;
import echipa_8.centenargo_core.wrappers.Landmark;

/**
 * Created by A6I3M2 on 09-May-18.
 */

public class ValidateCheck_Service extends AsyncTask<String, String, Object> {

    private WeakReference<Landmark_Activity> landmark_activity;

    public ValidateCheck_Service(final Landmark_Activity landmark_activity) {
        this.landmark_activity = new WeakReference<>(landmark_activity);
    }
    @Override
    protected Object doInBackground (String... strings) {
        try {
            URL url = new URL("http://10.0.2.2:8080/api/landmark/" + strings[0] + "/validateCheck");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/java");
            httpURLConnection.connect();

            String token = SharedPreferencesUtility.getToken();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            jsonObject.put("routeId", strings[1]);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(jsonObject.toString());
            dos.flush();
            dos.close();

            Integer replyCode = httpURLConnection.getResponseCode();
            String replyMessage = httpURLConnection.getResponseMessage();

            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            Boolean result = (Boolean) json.get("correct");

            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute (Object o) {
        super.onPostExecute(o);
        landmark_activity.get().validateCheck((Boolean) o);
    }
}
