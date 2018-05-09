package echipa_8.centenargo_app.services;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import echipa_8.centenargo_app.activities.Upload_Activity;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

/**
 * Created by Vlad on 08-May-18.
 */

public class Upload_Service extends AsyncTask<String, String, Object> {

    private WeakReference<Upload_Activity> upload_activity_;

    public Upload_Service(Upload_Activity upload_activity) {
        this.upload_activity_ = new WeakReference<>(upload_activity);
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
            URL url = new URL("http://192.168.1.4:8080/api/landmark/" + strings[0] + "/upload");

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.connect();

            String token = SharedPreferencesUtility.getToken();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            jsonObject.put("landmarkId", strings[0]);
            jsonObject.put("image",strings[1]);

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

            //JSONObject replyJSON = new JSONObject(response.toString());

            if (replyCode == 201) {
                Log.i("STATUS", replyCode.toString());
                Log.i("MESSAGE", replyMessage);
            } else {
                Log.w("ERROR", "Error code " + replyCode + ": " + replyMessage);
                return null;
            }

            return replyCode;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        this.upload_activity_.get().uploadComplete(null == o ? null : o.toString());
    }

}
