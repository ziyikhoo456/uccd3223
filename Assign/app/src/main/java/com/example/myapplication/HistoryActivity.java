package com.example.myapplication;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;
    String[][] display;
    CheckBox equalCB;
    CheckBox customCB;
    TableLayout tableLayout;
    String selection;
    String[] args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        mySQLiteAdapter = new SQLiteAdapter(this);
        equalCB = (CheckBox) findViewById(R.id.equalCB);
        customCB = (CheckBox) findViewById(R.id.customCB);
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);


        equalCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton equalCB, boolean b) {
                // Clear existing rows
                tableLayout.removeAllViews();

                //Display only equal break down result
                if (equalCB.isChecked() && (customCB.isChecked() == false)) {
                    selection = "Category =?";
                    args = new String[]{"Equal"};
                }

                //Display only custom break down result
                else if((equalCB.isChecked()==false) && customCB.isChecked()) {
                    selection = "Category =?";
                    args = new String[]{"Custom"};
                }

                //Display both break down result
                else if (equalCB.isChecked() && customCB.isChecked()){
                    selection = "Category =? OR Category =?";
                    args = new String[]{"Equal", "Custom"};

                } else { //don't display everything
                    return;
                }

                mySQLiteAdapter.openToRead();
                display = mySQLiteAdapter.queueSome(selection,args);

                //display the results in table
                for(int i = 0; i < mySQLiteAdapter.getCount(); i++){
                    TableRow row = new TableRow(HistoryActivity.this);
                    for(int j = 0; j < 5; j++){
                        TextView textView = new TextView(HistoryActivity.this);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT);
                        params.weight = 3.0f;
                        textView.setLayoutParams(params);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        textView.setText(display[i][j]);
                        row.addView(textView);
                    }
                    tableLayout.addView(row);
                }

                mySQLiteAdapter.close();

            }
        });

        customCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton customCB, boolean b) {
                // Clear existing rows
                tableLayout.removeAllViews();

                //Display the result of equal break down only
                if (equalCB.isChecked() && (customCB.isChecked() == false)) {
                    selection = "Category =?";
                    args = new String[]{"Equal"};
                }

                //Display the result of custom break down only
                else if((equalCB.isChecked()==false) && customCB.isChecked()) {
                    selection = "Category =?";
                    args = new String[]{"Custom"};

                }

                //Display the results of both break down
                else if (equalCB.isChecked() && customCB.isChecked()){
                    selection = "Category =? OR Category =?";
                    args = new String[]{"Equal", "Custom"};

                } else { //don't display anything
                    return;
                }

                mySQLiteAdapter.openToRead();
                display = mySQLiteAdapter.queueSome(selection,args);

                //Display the results in table
                for(int i = 0; i < mySQLiteAdapter.getCount(); i++){
                    TableRow row = new TableRow(HistoryActivity.this);
                    for(int j = 0; j < 5; j++){
                        TextView textView = new TextView(HistoryActivity.this);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT);
                        params.weight = 3.0f;
                        textView.setLayoutParams(params);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        textView.setText(display[i][j]);
                        row.addView(textView);
                    }
                    tableLayout.addView(row);
                }
                mySQLiteAdapter.close();
            }
        });





    }
}