package echipa_8.centenargo_app.services;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import echipa_8.centenargo_app.activities.Image_Activity;
import echipa_8.centenargo_app.activities.Landmark_Activity;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

/**
 * Created by A6I3M2 on 10-May-18.
 */

public class CheckLike_Service extends AsyncTask<String, String, Object> {

    private WeakReference<Image_Activity> image_activity;

    public CheckLike_Service(final Image_Activity image_activity) {
        this.image_activity = new WeakReference<>(image_activity);
    }
    @Override
    protected Object doInBackground (String... strings) {
        try {
            URL url = new URL("http://10.0.2.2:8080/api/gallery/" + strings[0] + "/checkLiked");

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

//    @Override
//    protected void onPostExecute (Object o) {
//        super.onPostExecute(o);
//        image_activity.get().validateCheck((Boolean) o);
//    }
}


