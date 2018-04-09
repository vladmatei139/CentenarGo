package echipa_8.centenargo_app.activities;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;

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
        
    }

}
