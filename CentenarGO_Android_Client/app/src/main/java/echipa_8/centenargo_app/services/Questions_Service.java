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

public class Questions_Service extends AsyncTask<String, String, Object> {

    private WeakReference<Questions_Activity> questions_activity;

    public Questions_Service(final Questions_Activity questions_activity) {
        this.questions_activity = new WeakReference<>(questions_activity);
    }

    @Override
    protected Object doInBackground (String... strings) {
        try {
            URL url = new URL("http://10.0.2.2:8080/api/landmark/" + strings[1] + "/questions/");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/java");
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", strings[0]);

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

            Map<Integer, Map<String, Object>> result = new HashMap<>();
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Map<String, Object> object = new HashMap<>();
                JSONObject obj = new JSONObject(json.get(key).toString());
                object.put("question", obj.get("question"));
                JSONArray answers = obj.getJSONArray("answers");
                List<Pair<Integer, String>> answersList = new ArrayList<>();
                for (int i = 0; i < answers.length(); i++) {
                    JSONObject answer = new JSONObject(answers.get(i).toString());
                    answersList.add(new Pair<>(Integer.valueOf((Integer) answer.get("id")), (String) answer.get("answer")));
                }
                object.put("answers", answersList);
                result.put(Integer.valueOf(key), object);
            }

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
        questions_activity.get().setQuestions((Map<Integer, Map<String, Object>>)(null == o ? null : o));
    }

}
