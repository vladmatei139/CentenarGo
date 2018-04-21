package echipa_8.centenargo_app.services;
import android.os.AsyncTask;
import android.util.Log;

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

import echipa_8.centenargo_app.activities.Landmark_Activity;
import echipa_8.centenargo_app.activities.Route_Activity;

/**
 * Created by Ioan-Emanuel Popescu on 16-Apr-18.
 */

public class Landmark_Service extends AsyncTask<String, String, Object> {

    private WeakReference<Landmark_Activity> landmark_activity_;

    public Landmark_Service(Landmark_Activity landmark_activity) {
        this.landmark_activity_ = new WeakReference<>(landmark_activity);
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
            URL url = new URL("http://10.0.2.2:8080/api/landmark" + "/" + strings[1]);

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.connect();


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", strings[0]);
            jsonObject.put("landmarkId", strings[1]);

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
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject replyJSON = new JSONObject(response.toString());

            StringBuilder sb = new StringBuilder();

            JSONObject landmarkJSON = new JSONObject(replyJSON.getString("landmark"));

            String id         = landmarkJSON.getString("id");
            String name       = landmarkJSON.getString("name");
            String content    = landmarkJSON.getString("content");
            String latitude   = landmarkJSON.getString("latitude");
            String longitude  = landmarkJSON.getString("longitude");
            String routeorder = landmarkJSON.getString("routeorder");
            String is_current = landmarkJSON.getString("is_current");

            sb.append(id + "," + name + "," + content + "," + latitude + ","
                    + longitude + "," + routeorder + "," + is_current + '\n');

            String routes = sb.toString();
            if (replyCode == 200) {
                Log.i("STATUS", replyCode.toString());
                Log.i("MESSAGE", replyMessage);
            } else {
                Log.w("ERROR", "Error code " + replyCode + ": " + replyMessage);
                return null;
            }

            return replyJSON;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        this.landmark_activity_.get().setLandmark(null == o ? null : o.toString());
    }

    protected String getFirstWords(String text, int n){
        String [] arr = text.split("\\s+");
        StringBuilder nWords = new StringBuilder();

        // concatenating number of words that you required
        for(int i=0; i < n ; i++){
            nWords.append(arr[i]);
            nWords.append(" ");
        }

        return nWords.toString();
    }
}
