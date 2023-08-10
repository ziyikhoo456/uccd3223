package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        //Get the value of amount and people from SharedPreferences
        SharedPreferences pref = getSharedPreferences("MySharedPreferences",0);
        float total = pref.getFloat("total",0);
        int ppl = pref.getInt("ppl",1);

        TextView amountTV = (TextView)findViewById(R.id.amountTV);
        TextView pplTV = (TextView)findViewById(R.id.pplTV);

        Button percentBtn = (Button) findViewById(R.id.percentBtn);
        Button ratioBtn = (Button) findViewById(R.id.ratioBtn);
        Button customBtn = (Button) findViewById(R.id.customBtn);

        amountTV.setText("RM "+String.format("%.2f", total));
        pplTV.setText(ppl+" People");

        Intent i = new Intent(this, InputActivity.class);

        percentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.putExtra("TYPE","percentage");
                startActivity(i);
            }
        });

        ratioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.putExtra("TYPE","ratio");
                startActivity(i);
            }
        });

        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.putExtra("TYPE","custom");
                startActivity(i);
            }
        });

    }
}