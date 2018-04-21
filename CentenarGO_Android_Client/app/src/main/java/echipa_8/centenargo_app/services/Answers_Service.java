package echipa_8.centenargo_app.services;

import android.os.AsyncTask;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import echipa_8.centenargo_app.activities.Questions_Activity;

public class Answers_Service extends AsyncTask<String, String, Object> {

    private WeakReference<Questions_Activity> questions_activity;

    public Answers_Service(final Questions_Activity questions_activity) {
        this.questions_activity = new WeakReference<>(questions_activity);
    }

    @Override
    protected Object doInBackground (String... strings) {
        try {
            URL url = new URL("http://192.168.1.2:8080/api/landmark/" + strings[1] + "/questions/validate-answers/");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/java");
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", strings[0]);
            jsonObject.put("answers", new JSONArray(strings[2]));

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

            //TODO update currentlandmark for user (new end-point)

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
        questions_activity.get().switchToRoute((Boolean) o);
    }



}
