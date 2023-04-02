package com.example.applicationdessin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AccueilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
    }

    public void createNewDraw(View view)
    {

        Intent intentActivity = new Intent(this, DessinActivity.class);
        startActivity(intentActivity);


    }


    public void reprendreDessin(View view)
    {

        Intent intentActivity = new Intent(this, DessinActivity.class);
        startActivity(intentActivity);


    }


    public void quitter(View view) {
        finish();
    }




}