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
import java.net.URLEncoder;

import echipa_8.centenargo_app.activities.Route_Activity;
import echipa_8.centenargo_app.activities.Routes_Activity;

/**
 * Created by sando on 3/30/2018.
 */

public class Route_Service extends AsyncTask<String, String, Object> {

    private WeakReference<Route_Activity> route_activity;

    public Route_Service(final Route_Activity route_activity) {
            this.route_activity = new WeakReference<>(route_activity);
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
            URL url = new URL("http://192.168.1.2:8080/api/route" + "/" + strings[1]);

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.connect();


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", strings[0]);
            jsonObject.put("routeId", strings[1]);

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

            JSONArray replyJSON = new JSONObject(response.toString()).getJSONArray("landmarks");

            StringBuilder sb = new StringBuilder();
            for (int n = 0; n < replyJSON.length(); n++)
            {
                JSONObject innerJObject = replyJSON.getJSONObject(n);
                String id = innerJObject.getString("id");
                String name = innerJObject.getString("name");
                String latitude = innerJObject.getString("latitude");
                String longitude = innerJObject.getString("longitude");
                sb.append(id + "," + name + "," + latitude + "," + longitude + '\n');
            }

            String routes = sb.toString();
            if (replyCode == 200) {
                Log.i("STATUS", replyCode.toString());
                Log.i("MESSAGE", replyMessage);
            } else {
                Log.w("ERROR", "Error code " + replyCode + ": " + replyMessage);
                return null;
            }

            return routes;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        this.route_activity.get().setLandmarks(null == o ? null : o.toString());
    }
}