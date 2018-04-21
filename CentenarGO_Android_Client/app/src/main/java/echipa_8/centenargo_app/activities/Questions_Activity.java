package echipa_8.centenargo_app.activities;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.Answers_Service;
import echipa_8.centenargo_app.services.Questions_Service;

public class Questions_Activity extends AppCompatActivity {

    Toolbar mActionBarToolbar;
    Map<Integer, Map<String, Object>> data;
    String token;
    Integer routeId;
    Integer landmarkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiYTMxOTRiOTYtYzQzMC00M2RlLTkxZGYtOTlmYmNkZWE1OWM0IiwiaWF0IjoxNTI0Mjk3Njc4LCJleHAiOjE1MjQzNDA4Nzh9.cNoW8T8Hvv0NSW39Dsu7iSLaqPUNlLDNruKPhHSC4oo";//intent.getStringExtra("TOKEN");
        setContentView(R.layout.activity_questions_);
        routeId = intent.getIntExtra("RouteId", 1);
        landmarkId = intent.getIntExtra("LandmarkId", 1);

        mActionBarToolbar = findViewById(R.id.toolbar_questions);
        mActionBarToolbar.setTitle("CentenarGo");
        setSupportActionBar(mActionBarToolbar);

        Questions_Service questions_service = new Questions_Service(this);
        questions_service.execute(token, Integer.toString(landmarkId), Integer.toString(routeId));
    }

    public void setQuestions(Map<Integer, Map<String, Object>> response) {
        data = response;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parent = inflater.inflate(R.layout.activity_questions_, null);
        LinearLayout questions = (LinearLayout) parent.findViewById(R.id.layout_questions);

        for (Integer key : data.keySet()) {
            View currentQuestion = inflater.inflate(R.layout.question_template, null);
            ((TextView) currentQuestion.findViewById(R.id.question_text)).setText((String) data.get(key).get("question"));
            RadioGroup answerGroup = currentQuestion.findViewById(R.id.question_answer_group);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            for (Pair<Integer, String> answer : (List<Pair<Integer, String>>) data.get(key).get("answers")) {
                RadioButton answerButton = new RadioButton(this);
                answerButton.setId(answer.first);
                answerButton.setText(answer.second);
                answerGroup.addView(answerButton, params);
            }
            questions.addView(currentQuestion);
        }

        setContentView(parent);
    }

    private void toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void finishAction(View view) {
        List<Integer> answers = new ArrayList<>();
        LinearLayout questions = (LinearLayout) findViewById(R.id.layout_questions);
        for (int i = 0; i < questions.getChildCount(); i++) {
            RadioGroup answerGroup = (RadioGroup) questions.getChildAt(i);
            boolean found = false;
            for (int j = 0; !found && j < answerGroup.getChildCount(); j++) {
                RadioButton answerButton = (RadioButton) answerGroup.getChildAt(j);
                if (answerButton.isChecked()) {
                    found = true;
                    answers.add(answerButton.getId());
                }
            }
            if (!found) {
                toast("Nu au fost date raspunsuri pentru toate intrebarile");
                return;
            }
        }
        Answers_Service answers_service = new Answers_Service(this);
        answers_service.execute(token, Integer.toString(landmarkId), answers.toString());
    }

    public void switchToRoute(Boolean correct) {
        toast(correct ? "Raspunsurile sunt corecte" : "Cel putin un raspuns este gresit");
        Intent intent = new Intent(getApplicationContext(), Route_Activity.class);
        intent.putExtra("RouteId", routeId);
        intent.putExtra("TOKEN", token);
        startActivity(intent);
    }

}
