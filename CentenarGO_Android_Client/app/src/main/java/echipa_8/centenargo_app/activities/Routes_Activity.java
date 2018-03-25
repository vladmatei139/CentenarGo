package com.example.ioan_emanuelpopescu.centenargov2.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ioan_emanuelpopescu.centenargov2.R;
import com.example.ioan_emanuelpopescu.centenargov2.adapters.RecyclerViewRouteAdapter;
import com.example.ioan_emanuelpopescu.centenargov2.wrappers.Landmark;
import com.example.ioan_emanuelpopescu.centenargov2.wrappers.Route;

import java.util.ArrayList;
import java.util.Arrays;

public class Routes_Activity extends AppCompatActivity {

    private ArrayList<Route> availableRoutes = new ArrayList<>();
    private RecyclerView recyclerView;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_);

        availableRoutes.addAll(Arrays.asList(new Route(1,"a",new ArrayList<Landmark>(), 0, false)));

        availableRoutes.add(Route.generateMockup());
        availableRoutes.add(Route.generateMockup());
        availableRoutes.add(Route.generateMockup());
        availableRoutes.add(Route.generateMockup());

        rvAdapter = new RecyclerViewRouteAdapter(availableRoutes);
        recyclerView = findViewById(R.id.recycleViewRoute);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rvAdapter);

    }

}
