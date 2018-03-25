package com.example.ioan_emanuelpopescu.centenargov2.services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ioan_emanuelpopescu.centenargov2.activities.Login_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

//  Params, Progress, Result
public class Login_Service extends AsyncTask<String,String, Object> {

    @Override
    protected Object doInBackground(String... strings) {
        try {
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
            JSONObject replyJSON = new JSONObject();

            if(replyCode == 200){
                replyJSON = new JSONObject(replyMessage);
                Log.i("STATUS", String.valueOf(httpURLConnection.getResponseCode()));
                Log.i("MESSAGE", httpURLConnection.getResponseMessage());

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
        if(o == null){
            Login_Activity.setResponse(null);
        } else {
            Login_Activity.setResponse(o.toString());
        }
    }


}
