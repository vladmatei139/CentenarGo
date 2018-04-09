package echipa_8.centenargo_app.activities;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.Questions_Service;

public class Questions_Activity extends AppCompatActivity {

    Toolbar mActionBarToolbar;
    Map<Integer, Map<String, Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String token = intent.getStringExtra("TOKEN");
        setContentView(R.layout.activity_questions_);
        int routeId = intent.getIntExtra("LandmarkId", 0);

        mActionBarToolbar = findViewById(R.id.toolbar_questions);
        mActionBarToolbar.setTitle("CentenarGo");
        setSupportActionBar(mActionBarToolbar);

        Questions_Service questions_service = new Questions_Service(this);
        questions_service.execute(token, Integer.toString(routeId));
    }

    public void setQuestions(Map<Integer, Map<String, Object>> response) {
        data = response;
        RecyclerView questionsView = findViewById(R.id.recyclerView_questions);
        LinearLayout question_template = findViewById(R.id.question_template);
        int i = 0;
        for (Integer key : data.keySet()) {
            questionsView.addView(question_template);
            ViewGroup current_question = (ViewGroup) questionsView.getChildAt(i);
            ((TextView) current_question.getChildAt(0)).setText((String) data.get(key).get("question"));
            //TODO add the answers for the current question
            i++;
        }
    }

}
