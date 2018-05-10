package echipa_8.centenargo_app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.Answers_Service;
import echipa_8.centenargo_app.services.Questions_Service;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

public class Questions_Activity extends AppCompatActivity {

    Toolbar mActionBarToolbar;
    Map<Integer, Map<String, Object>> data;
    Integer routeId;
    Integer landmarkId;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private void setNavigationBar() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.questions_drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()){
                case R.id.menu_gallery:
                    intent = new Intent(this.getApplicationContext(), Gallery_Activity.class);
                    this.startActivity(intent);
                    return true;

                case R.id.menu_routes:
                    intent = new Intent(this.getApplicationContext(), Routes_Activity.class);
                    this.startActivity(intent);
                    return true;

                case R.id.menu_sign_off:
                    Toast.makeText(this, "You've been logged out", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), Login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;

                case R.id.menu_tutorial:
                    intent = new Intent(this.getApplicationContext(), Intro_Activity.class);
                    startActivity(intent);
                    return true;

                case R.id.menu_stats:
                    // todo: Start Statistics intent
                    return true;

                default:
                    return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String token = SharedPreferencesUtility.getToken();
        setContentView(R.layout.activity_questions_);
        routeId = intent.getIntExtra(getString(R.string.route_id_key), 0);
        landmarkId = intent.getIntExtra(getString(R.string.landmark_id_key), 0);

        mActionBarToolbar = findViewById(R.id.toolbar_questions);
        mActionBarToolbar.setTitle("CentenarGo");
        setSupportActionBar(mActionBarToolbar);

        setNavigationBar();

        Questions_Service questions_service = new Questions_Service(this);
        questions_service.execute(Integer.toString(landmarkId), Integer.toString(routeId));
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
            RadioGroup answerGroup = (RadioGroup)((LinearLayout)questions.getChildAt(i)).getChildAt(1);
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
        answers_service.execute(Integer.toString(landmarkId), answers.toString(), routeId.toString());
    }

    public void switchToRoute(Boolean correct) {
        toast(correct ? "Raspunsurile sunt corecte" : "Cel putin un raspuns este gresit");
        if (correct) {
            Intent intent = new Intent(getApplicationContext(), Route_Activity.class);
            intent.putExtra(getString(R.string.route_id_key), routeId);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Landmark_Activity.class);
            intent.putExtra(getString(R.string.route_id_key), routeId);
            intent.putExtra(getString(R.string.landmark_id_key), landmarkId);
            startActivity(intent);
        }
        finish();
    }

}
